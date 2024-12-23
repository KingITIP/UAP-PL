import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ArmorDataApp {
    private JFrame frame;
    private JTextField nameField, levelField, typeField, rarityField;
    private JLabel imageLabel;
    private String imagePath;
    private DefaultTableModel tableModel;
    private JTable armorTable;
    private ArrayList<Armor> armorList;

    public ArmorDataApp() {
        armorList = new ArrayList<>();
        frame = new JFrame("Armor Data App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        nameField = new JTextField();
        levelField = new JTextField();
        typeField = new JTextField();
        rarityField = new JTextField();

        JButton imageButton = new JButton("Select Image");
        imageLabel = new JLabel("No image selected");
        imageButton.addActionListener(e -> selectImage());

        inputPanel.add(new JLabel("Name: "));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Level: "));
        inputPanel.add(levelField);
        inputPanel.add(new JLabel("Type: "));
        inputPanel.add(typeField);
        inputPanel.add(new JLabel("Rarity: "));
        inputPanel.add(rarityField);
        inputPanel.add(imageButton);
        inputPanel.add(imageLabel);

        JPanel buttonPanel = new JPanel();
        JButton createButton = new JButton("Create");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton saveButton = new JButton("Save to File");
        JButton loadButton = new JButton("Load Data");

        createButton.addActionListener(e -> createArmor());
        updateButton.addActionListener(e -> updateArmor());
        deleteButton.addActionListener(e -> deleteArmor());
        saveButton.addActionListener(e -> saveToFile());
        loadButton.addActionListener(e -> loadFromFile());

        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        // Table setup
        String[] columnNames = {"Name", "Level", "Type", "Rarity", "Image"};
        tableModel = new DefaultTableModel(columnNames, 0);
        armorTable = new JTable(tableModel);
        armorTable.getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());
        armorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(armorTable), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            imageLabel.setText("Image Selected");
        }
    }

    private void createArmor() {
        String name = nameField.getText();
        String level = levelField.getText();
        String type = typeField.getText();
        String rarity = rarityField.getText();

        if (name.isEmpty() || level.isEmpty() || type.isEmpty() || rarity.isEmpty() || imagePath == null) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields and select an image.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Armor armor = new Armor(name, level, type, rarity, imagePath);
        armorList.add(armor);
        tableModel.addRow(new Object[]{name, level, type, rarity, imagePath});
        clearFields();
    }

    private void updateArmor() {
        int selectedIndex = armorTable.getSelectedRow();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText();
        String level = levelField.getText();
        String type = typeField.getText();
        String rarity = rarityField.getText();

        if (name.isEmpty() || level.isEmpty() || type.isEmpty() || rarity.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Armor armor = armorList.get(selectedIndex);
        armor.setName(name);
        armor.setLevel(level);
        armor.setType(type);
        armor.setRarity(rarity);
        if (imagePath != null) {
            armor.setImagePath(imagePath);
        }

        tableModel.setValueAt(name, selectedIndex, 0);
        tableModel.setValueAt(level, selectedIndex, 1);
        tableModel.setValueAt(type, selectedIndex, 2);
        tableModel.setValueAt(rarity, selectedIndex, 3);
        tableModel.setValueAt(armor.getImagePath(), selectedIndex, 4);

        clearFields();
    }

    private void deleteArmor() {
        int selectedIndex = armorTable.getSelectedRow();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        armorList.remove(selectedIndex);
        tableModel.removeRow(selectedIndex);
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data_Armor.txt"))) {
            for (Armor armor : armorList) {
                writer.write(armor.getName() + "," + armor.getLevel() + "," + armor.getType() + "," + armor.getRarity() + "," + armor.getImagePath());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(frame, "Data saved to Data_Armor.txt", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Data_Armor.txt"))) {
            String line;
            armorList.clear();
            tableModel.setRowCount(0);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Armor armor = new Armor(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    armorList.add(armor);
                    tableModel.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3], parts[4]});
                }
            }
            JOptionPane.showMessageDialog(frame, "Data loaded from Data_Armor.txt", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        levelField.setText("");
        typeField.setText("");
        rarityField.setText("");
        imageLabel.setText("No image selected");
        imagePath = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ArmorDataApp::new);
    }
}
