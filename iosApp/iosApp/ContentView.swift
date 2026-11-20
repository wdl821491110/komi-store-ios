import SwiftUI
import ComposeApp

struct ContentView: View {
    var body: some View {
        ComposeViewControllerToSwiftUI()
            .ignoresSafeArea()
    }
}

struct ComposeViewControllerToSwiftUI: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
