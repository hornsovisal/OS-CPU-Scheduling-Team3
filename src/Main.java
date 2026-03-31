import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.MainWindow;

public class Main {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
			// Fall back to default look and feel if system look and feel is unavailable.
		}

		SwingUtilities.invokeLater(() -> {
			MainWindow window = new MainWindow();
			window.setVisible(true);
		});
	}
}
