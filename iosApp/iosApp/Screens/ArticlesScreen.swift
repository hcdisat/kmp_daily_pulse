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
                }
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
    var article: Article
    
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

#Preview {
    ArticlesScreen()
}
