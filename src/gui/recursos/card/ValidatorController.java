package gui.recursos.card;

import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import util.Validator;
import java.util.HashMap;
import java.util.Map;

public class ValidatorController extends Controller {
    protected Map<Control, Boolean> interruptorMap = new HashMap<>();

    protected boolean verifyInputData() {
        boolean dataInputValid = true;
        for( Map.Entry<Control, Boolean> entry : interruptorMap.entrySet() ) {
            if( !entry.getValue() ){
                dataInputValid = false;
                break;
            }
        }
        return dataInputValid;
    }

    protected void initValidatorToTextInputControl(Map<TextInputControl, Object[]> validatorMap) {
        final int FIRST_CONTRAINT = 0;
        final int SECOND_CONTRAINT = 1;
        for (Map.Entry<TextInputControl, Object[]> entry : validatorMap.entrySet() ) {
            entry.getKey().textProperty().addListener( (observable, oldValue, newValue) -> {
                if( !Validator.doesStringMatchPattern( newValue, ( (String) entry.getValue()[FIRST_CONTRAINT] ) ) || Validator.isStringLargerThanLimitOrEmpty( newValue, ( (Integer) entry.getValue()[SECOND_CONTRAINT] ) ) ){
                    interruptorMap.put(entry.getKey(), false );
                    entry.getKey().getStyleClass().clear();
                    entry.getKey().getStyleClass().add("wrongTextField");
                } else {
                    interruptorMap.put(entry.getKey(), true );
                    entry.getKey().getStyleClass().clear();
                    entry.getKey().getStyleClass().add("correctlyTextField");
                }
            });
        }
    }

}
