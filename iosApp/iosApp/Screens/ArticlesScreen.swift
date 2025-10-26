//
//  ArticlesScreen.swift
//  iosApp
//
//  Created by Hector Barreiro on 9/28/25.
//

import SwiftUI
import ComposeApp
import Foundation

struct ArticlesScreen: View {
    private(set) var viewModel: ArticlesViewModel
    
    init() {
        viewModel = DiHelper().getArticlesViewModel()
    }
    
    var body: some View {
        Observing(viewModel.articles) { state in
            VStack {
                AppBar()
                
                if state.isLoading {
                    Loader()
                }
                
                if let error = state.error {
                    ErrorView(message: error)
                }
                
                let articles = state.articles
                if (!articles.isEmpty) {
                    ScrollView {
                        LazyVStack(spacing: 10) {
                            ForEach(articles, id: \.self) { article in
                                ArticleItemView(article: article)
                            }
                        }
                    }
                } else {
                    EmptyArticleView(onRefresh: {
                        viewModel.onEvent(action: .Refresh())
                    })
                }
                
                Spacer()
            }.refreshable {
                viewModel.onEvent(action: .Refresh())
            }
        }
    }
}

struct AppBar: View {
    var body: some View {
        Text("Articles")
            .font(.largeTitle)
            .fontWeight(.bold)
    }
}

struct Loader: View {
    var body: some View {
        ProgressView()
    }
}

struct ErrorView: View {
    var message: String
    
    var body: some View {
        Text(message).font(.title)
    }
}

struct ArticleItemView: View {
    var article: ArticleUI
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            AsyncImage(url: URL(string: article.imageUrl)) { phase in
                if phase.image != nil {
                    phase.image!
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                } else if phase.error != nil {
                    Text("Image Load Error")
                } else {
                    ProgressView()
                }
            }
            Text(article.title).font(.title).fontWeight(.bold)
            Text(article.desc)
            Text(article.date)
                .frame(maxWidth: .infinity, alignment: .trailing)
                .foregroundStyle(.gray)
        }
        .padding(16)
        .onAppear {
            NSLog("ArticleId: \(article.id) - Description: \(article.desc)")
        }
    }
}


private struct EmptyArticleView: View {
    
    @State var isLoading: Bool = false
    
    var onRefresh: () -> Void = { }
    
    var body: some View {
        VStack {
            Spacer()
            
            Text("No articles found")
                .font(.title)
                .fontWeight(.bold)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            Text("No articles found on any sources at the moment. You can refresh below")
                .font(.system(size: 16, design: .default))
                .frame(maxWidth: .infinity, alignment: .leading)
            
            Button {
                guard !isLoading else { return }
                
                isLoading = true
                onRefresh()
                
            } label: {
                HStack {
                    Text("Refresh")
                    ProgressView().opacity(isLoading ? 1 : 0)
                }
            }
            .disabled(isLoading)
            .padding(16)
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(.white)
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(Color.gray.opacity(0.3), lineWidth: 1)
                    )
            )
            
            Spacer()
            Spacer()
                
        }.padding([.leading, .trailing], 16)
    }
}

#Preview {
    ArticlesScreen()
}

