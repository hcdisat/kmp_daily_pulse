import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinInitKt.doInitKoinIOS()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
