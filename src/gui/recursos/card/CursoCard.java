package gui.recursos.card;

import domain.Curso;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CursoCard extends VBox{
    private final int CARD_HEIGHT = 121;
    private final int CARD_WIDTH = 278;
    private final int FONT_SIZE = 16;
    private final int FONT_SMALL_SIZE = 10;
    private final int SPACING = 10;
    private final int TOP_INSET = 5;
    private final int BOTTOM_INSET = 5;
    private final int LEFT_INSET = 5;
    private final int RIGHT_INSET = 5;
    private Curso curso;

    public CursoCard(Curso curso) {
        this.curso = curso;
        initCard();
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void repaint() {
        getChildren().clear();
        initCard();
    }

    private void initCard() {
        Label nombreCurso = new Label( curso.getNombre() );
        nombreCurso.setFont( Font.font("Open Sans", FONT_SIZE) );
        nombreCurso.setWrapText(true);
        Label docenteLabel = new Label( (curso.getDocente() != null) ? curso.getDocente().getNombre() : "" );
        docenteLabel.setFont( Font.font("Open Sans", FONT_SMALL_SIZE) );
        docenteLabel.getStyleClass().add("cardSmallLabel");
        docenteLabel.setWrapText(true);
        setPrefWidth(CARD_WIDTH);
        setPrefHeight(CARD_HEIGHT);
        setMaxWidth(CARD_WIDTH);
        setMaxHeight(CARD_HEIGHT);
        setCursor(Cursor.HAND);
        setPadding(new Insets( TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET) );
        setSpacing(SPACING);
        setAlignment(Pos.TOP_RIGHT);
        getChildren().add(nombreCurso);
        getChildren().add(docenteLabel);
        setFocusTraversable(true);
        setStyle("-fx-border-color:#C0C9D3;-fx-border-width:1 1 1 1;");
        setOnMouseEntered((MouseEvent event) -> { setStyle("-fx-border-color:#0043ff;-fx-border-width:1 1 1 1;"); });
        setOnMouseExited((MouseEvent event) -> { setStyle("-fx-border-color:#C0C9D3;-fx-border-width:1 1 1 1;"); });
    }

}
