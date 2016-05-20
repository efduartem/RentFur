package rentfur.receipt;

import rentfur.util.MainWindowController;
import rentfur.util.UserRoles;

/**
 *
 * @author FDuarte
 */
public class ReceiptController {
    private MainWindowController mainWindowController;
    private ReceiptCreate receiptCreate;
    private UserRoles userRoles;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final String TABLE_NAME = "furniture";
    public static final String ALL_VALUES = "Todos";
    
    public ReceiptCreate getReceiptCreate(){
        if(receiptCreate == null){
            receiptCreate = new ReceiptCreate(this);
        }
        return receiptCreate;
    }
    
    public void receiptCreateClosed(){
        receiptCreate = null;
    }
    
}
