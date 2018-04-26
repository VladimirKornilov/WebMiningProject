package com.tstu.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.tstu.io.InputTypeEnum;
import com.tstu.io.Controller;
import java.awt.Color;

public class MainWindow {

	public JTextField getTextField() {
		return textField;
	}
	
	public InputTypeEnum getInputType() {
		return inputType;
	}

	private JFrame frame;
	private JTextField textField;
	private JTextArea txtrLogs;

	private InputTypeEnum inputType = InputTypeEnum.CONFIG;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.control);
		frame.setBounds(100, 100, 843, 689);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton btnNewButton_2 = new JButton("X");
		btnNewButton_2.setBackground(Color.WHITE);
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField.setText("");
			}
		});
		
		// -----------------------------RADIO BUTTONS------------------------------------------------
		ButtonGroup group = new ButtonGroup();

		JRadioButton rdbtnUseAConfig = new JRadioButton("Use a config file", true);
		rdbtnUseAConfig.setFont(new Font("Tahoma", Font.PLAIN, 17));
		rdbtnUseAConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setEnabled(false);
				btnNewButton_2.setEnabled(false);
				inputType = InputTypeEnum.CONFIG;
			}
		});
		group.add(rdbtnUseAConfig);

		JRadioButton rdbtnTypeManually = new JRadioButton("Type manually", false);
		rdbtnTypeManually.setFont(new Font("Tahoma", Font.PLAIN, 17));
		rdbtnTypeManually.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField.setEnabled(true);
				btnNewButton_2.setEnabled(true);
				inputType = InputTypeEnum.MANUAL;
			}
		});
		group.add(rdbtnTypeManually);
		
		JRadioButton rdbtnSearch = new JRadioButton("Search");
		
		rdbtnSearch.setFont(new Font("Tahoma", Font.PLAIN, 17));
		rdbtnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField.setEnabled(true);
				btnNewButton_2.setEnabled(true);
				inputType = InputTypeEnum.SEARCH;
			}
		});
		
		group.add(rdbtnSearch);
		//--------------------------------------------------------------------------------------
		
		JTextPane txtpnHowToChoose = new JTextPane();
		txtpnHowToChoose.setFont(new Font("Tahoma", Font.PLAIN, 17));
		txtpnHowToChoose.setEditable(false);
		txtpnHowToChoose.setBackground(SystemColor.control);
		txtpnHowToChoose.setText("How to choose needed sites?");

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textField.setEnabled(false);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("Start");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					Controller.window = MainWindow.this;
					Thread thread = new Controller();
					thread.start();
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnNewButton_1 = new JButton("Clear");
		btnNewButton_1.setBackground(Color.WHITE);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtrLogs.setText("Logs:");
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnSearch)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 486, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
						.addComponent(txtpnHowToChoose, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(rdbtnUseAConfig)
						.addComponent(rdbtnTypeManually))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtpnHowToChoose, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnTypeManually)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnUseAConfig)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnSearch)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
						.addComponent(btnNewButton_2, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 376, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
						.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
					.addGap(17))
		);

		txtrLogs = new JTextArea();
		scrollPane.setViewportView(txtrLogs);
		txtrLogs.setEditable(false);
		txtrLogs.setFont(new Font("Tahoma", Font.PLAIN, 17));
		txtrLogs.setText("Logs:");
		frame.getContentPane().setLayout(groupLayout);
	}

	public void addToLogs(String log) {
		txtrLogs.append("\n" + log);
	}
}
