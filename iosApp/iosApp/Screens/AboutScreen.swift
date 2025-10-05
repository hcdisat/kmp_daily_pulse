//
//  AboutScreen.swift
//  iosApp
//
//  Created by Hector Barreiro on 9/27/25.
//

import SwiftUI

struct AboutScreen: View {
    @Environment(\.dismiss)
    private var dismiss
    
    var body: some View {
        NavigationStack {
            AboutListView()
                .navigationTitle("About Device")
                .toolbar {
                    ToolbarItem(placement: .primaryAction) {
                        Button {
                            dismiss()
                        } label: {
                            Text("Done").bold()
                        }
                    }
            }
        }
    }
}

#Preview {
    AboutScreen()
}
