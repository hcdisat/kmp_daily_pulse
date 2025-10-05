import UIKit
import SwiftUI
import ComposeApp

//struct ComposeView: UIViewControllerRepresentable {
//    func makeUIViewController(context: Context) -> UIViewController {
//        MainViewControllerKt.MainViewController()
//    }
//
//    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
//}

struct ContentView: View {
//    var body: some View {
//        ComposeView()
//            .ignoresSafeArea()
//    }+
    
    @State private var shouldShowAboutScreen = false
    
    var body: some View {
        NavigationStack {
            ArticlesScreen().toolbar {
                ToolbarItem {
                    Button {
                        shouldShowAboutScreen = true
                    } label: {
                        Label("About", systemImage: "info.circle").labelStyle(.titleAndIcon)
                    }
                    .popover(isPresented: $shouldShowAboutScreen) {
                        AboutScreen()
                    }
                }
            }
        }
    }
}



