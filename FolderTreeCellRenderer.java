import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class FolderTreeCellRenderer extends DefaultTreeCellRenderer {

    private Icon folderIcon;
    private Icon fileIcon;

    public FolderTreeCellRenderer() {
        folderIcon = UIManager.getIcon("FileView.directoryIcon"); // Get the default folder icon
        fileIcon = UIManager.getIcon("FileView.fileIcon"); // Get the default file icon
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();

            if (userObject instanceof UserGroup) {
                setIcon(folderIcon); // Set folder icon for UserGroup nodes
            } else if (userObject instanceof User) {
                setIcon(fileIcon); // Set file icon for User nodes
            }
        }

        return component;
    }
}