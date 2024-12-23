import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.io.File;

class ImageRenderer implements TableCellRenderer {
    @Override
    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        if (value != null) {
            String imagePath = value.toString();
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                label.setIcon(icon);
            } else {
                label.setText("No Image");
            }
        }
        return label;
    }
}
