import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * @author Paul Grigoras
 */

public class PopupListener extends MouseAdapter {
  FunctionGrapher mjp;
	PopupListener(FunctionGrapher mjp){
		this.mjp=mjp;
	}
	public void mousePressed(MouseEvent e) {
         maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            mjp.popup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }
}
