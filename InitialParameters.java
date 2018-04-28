import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InitialParameters extends JPanel implements ActionListener {
	
	InitialParameters() {
    }

	void init() {
		setLayout(new GridLayout(m.size() + 1, 2));
	}

	private Map<String, JTextField> m = new HashMap<String, JTextField>();
	// The values for all parameters are stored in the map m

	public void add(String param, String description, String _default) {
		JLabel label = new JLabel(description);
		JTextField jtf = new JTextField(_default);

		add(label);
		add(jtf);

		m.put(param, jtf);

		setLayout(new GridLayout(m.size() + 1, 2, 10, 0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (Map.Entry<String, JTextField> entry : m.entrySet()) {
			System.out.print(entry.getKey());
			System.out.print(" : ");
			System.out.print(entry.getValue().getText());
			System.out.println();
		}
	}

	public static void main(String[] arg) {
		JFrame f = new JFrame();
		InitialParameters p = new InitialParameters();
		p.add("name", "Vad heter du", "Kalle");
		p.add("age", "Hur gammal är du", "123");
		p.add("color", "Vad är din favoritfärg", "blå");
		p.add("finger", "Hur många fingrar håller jag upp", "3");
		p.add("friday", "När är det fredag?", "idag");
		p.add("rain", "Regnar det?", "ja");

		JButton klar = new JButton("Klar!");

		p.add(klar);
		klar.addActionListener(p);

		f.add(p);
		f.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(screenSize.width / 2 - f.getWidth() / 2, screenSize.height / 2 - f.getHeight() / 2);

		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
