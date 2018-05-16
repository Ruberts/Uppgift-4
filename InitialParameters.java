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

/*
 * Author: Fredrik Robertsson
 * E-mail: fredrik.c.robertsson@gmail.com
 * 
 */

public class InitialParameters extends JPanel implements ActionListener {
	JFrame f;
	
	InitialParameters() {
		f = new JFrame("Set parameters of pasture or use the default");
		
		add("wolfSpeed", " Speed of the wolf?", "5");
		add("wolfView", " View distance of the wolf?", "4");
		add("sheepSpeed", " Speed of the sheep?", "4");
		add("sheepView", " View distance of the sheep?", "3");
		add("plantSpread", " How fast should the plants spread?", "50");
		
		JButton klar = new JButton("Klar!");
		add(klar);
		klar.addActionListener(this);
		
		f.add(this);
		f.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(screenSize.width / 2 - f.getWidth() / 2, screenSize.height / 2 - f.getHeight() / 2);

		f.setVisible(true);
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
		
		/* Remove the window with parameter input */
		f.dispose();
		
		/* Start the pasture simulation */
		new Pasture(
				Integer.parseInt(m.get("wolfSpeed").getText()),
				Integer.parseInt(m.get("wolfView").getText()),
				Integer.parseInt(m.get("sheepSpeed").getText()),
				Integer.parseInt(m.get("sheepView").getText()),
				Integer.parseInt(m.get("plantSpread").getText()));
	}
}
