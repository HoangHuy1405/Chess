package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseEventHandler implements MouseListener {
    private JLabel currentPiece;

    public void mousePressed(MouseEvent e) {
        /*identifySquare(e, "Mouse pressed");*/

    }

    public void mouseReleased(MouseEvent e) {
        /*identifySquare(e, "Mouse released");*/
    }

    public void mouseEntered(MouseEvent e) {
        /*identifySquare(e, "Mouse entered");*/
    }

    public void mouseExited(MouseEvent e) {
        /*identifySquare(e, "Mouse exited");*/
    }

    private static boolean inPlacingState = false;
    public void mouseClicked(MouseEvent e) {
        identifySquare(e, "Mouse clicked");
        JPanel clickedPanel = (JPanel) e.getSource();
        // that tile is empty and is inPlacingState (true)
        if(inPlacingState) {
            if(clickedPanel.getComponentCount() == 0) {
                clickedPanel.add(currentPiece);
                System.out.println("placing " +  currentPiece.getName());
                inPlacingState = false;
            }
        } else {
            for(Component c : clickedPanel.getComponents()) {
                // this means it not in placing state (not pick up the piece)
                if(c instanceof JLabel) {
                    currentPiece = (JLabel) c;
                    System.out.println("picking up " + currentPiece.getName());
                    clickedPanel.remove(c);
                    inPlacingState = true;
                }
            }
        }
        // Refresh the panel to reflect changes
        clickedPanel.revalidate();
        clickedPanel.repaint();
    }

    void saySomething(String eventDescription, MouseEvent e) {
        /*textArea.append(eventDescription + " detected on "
                + e.getComponent().getClass().getName()
                + "." + newline);*/
        System.out.println(eventDescription + " " + e.getPoint());
    }
    private void identifySquare(MouseEvent e, String eventType) {
        // Get the clicked panel
        JPanel panel = (JPanel) e.getComponent();

        // Retrieve row and column properties
        int row = (int) panel.getClientProperty("row");
        int col = (int) panel.getClientProperty("col");

        // Output the event type and panel position
        System.out.println(eventType + " on square: (" + row + ", " + col + ")");
    }

}
