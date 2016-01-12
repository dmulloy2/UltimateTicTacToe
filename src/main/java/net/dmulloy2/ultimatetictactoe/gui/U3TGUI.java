/**
 * Copyright (c) 2015 Dan Mulloy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.dmulloy2.ultimatetictactoe.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.dmulloy2.ultimatetictactoe.U3T;
import net.dmulloy2.ultimatetictactoe.types.Box;
import net.dmulloy2.ultimatetictactoe.types.MathUtil;

/**
 * @author Dan Mulloy
 */
public class U3TGUI extends JFrame {
	private static final long serialVersionUID = - 5224324241899628439L;

	private final U3T main;

	private JTextField outputField;
	private JMenuBar menuBar;

	private JPanel keyPanel;
	private Key key;

	public U3TGUI(final U3T main) {
		super("Ultimate TicTacToe by Dan Mulloy");
		this.main = main;

		init();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setVisible(true);

				MajorGrid major = main.getMajorGrid();
				setSize(major.getWidth() + 15, major.getHeight() + 80);
			}
		});

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void init() {
		getContentPane().setLayout(null);
		// getContentPane().setBackground(new Color(36, 64, 140));

		// ---- Menu Bar
		menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				main.save();
			}
		});

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		fileMenu.add(save);
		fileMenu.add(exit);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		// ----

		// Maximize it
		//setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

		Rectangle resolution = getGraphicsConfiguration().getBounds();

		// Fill most of the screen
		MajorGrid grid = main.getMajorGrid();
		int width = MathUtil.multipleInBounds(75, (int) resolution.getWidth() - 100,
				(int) resolution.getHeight() - 100);
		grid.setBounds(0, 0, width, width);
		grid.init();

		// Add the main grid
		getContentPane().add(grid);

		// Add the key for now, we don't know enough to actually do anything with it for now.
		key = new Key(main, Box.MIDDLE, 10);
		key.setVisible(false);

		keyPanel = new JPanel();
		keyPanel.setLayout(new GridLayout(1, 1, 10, 10));
		keyPanel.add(key);
		keyPanel.setVisible(false);

		getContentPane().add(keyPanel);

		// Text at the bottom
		outputField = new JTextField("", 10);
		outputField.setLocation(0, width);
		outputField.setSize(width, 20);
		outputField.setEditable(false);
		outputField.setBackground(Color.WHITE);

		getContentPane().add(outputField);
	}

	public void logInBox(String message) {
		outputField.setText(message);
	}

	public Key getKey() {
		return key;
	}

	public void createKey() {
		// Make it slightly to the right of the middle right box, like 1/3rd of the free width
		Rectangle resolution = getGraphicsConfiguration().getBounds();
		int gridWidth = main.getMajorGrid().getWidth();
		int freeWidth = (int) resolution.getWidth() - gridWidth;

		MinorGrid minor = main.getMajorGrid().getGrid(Box.MIDDLE_RIGHT);
		Rectangle bounds = minor.getBounds();

		int x = (int) (bounds.getX() + (freeWidth / 3));
		int y = (int) bounds.getY();
		int width = (int) bounds.getWidth();

		keyPanel.setBounds(x, y, width, width);
		keyPanel.setBackground(Color.DARK_GRAY);
		keyPanel.setVisible(true);

		//key.setBounds(x, y, width, width);
		key.init();
		key.setVisible(true);

		setSize(x + width + 30, getHeight());
	}
}
