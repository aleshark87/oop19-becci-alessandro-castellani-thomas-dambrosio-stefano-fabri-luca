package guicontrollers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import controller.GameController;
import controller.StandardGameControllerImpl;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import model.roundenvironment.barriers.Barrier;
import model.roundenvironment.barriers.Barrier.Orientation;
import model.roundenvironment.coordinate.Coordinate;
import model.roundenvironment.powerups.PowerUp;
import model.roundenvironment.powerups.PowerUp.Type;

public class LogicImpl implements Logic{
	
	private UIController view;
	private Optional<String> player1;
	private Optional<String> player2;

	private Map<Coordinate, BorderPane> gridMap;
	
	//0 for vertical, 1 for horizontal
	private Optional<Integer> selectedBarrier;

	public LogicImpl(UIController view) {
		this.view = view;
    	this.gridMap = new HashMap<Coordinate, BorderPane>();
    	this.selectedBarrier = Optional.empty();
	}
	
	@Override
	public void setPlayers(Optional<String> player1, Optional<String> player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
	
	@Override
	public BorderPane addPaneLogic(Coordinate position, GameController controller) {
		BorderPane pane = new BorderPane();
        pane.setOnMouseClicked(e -> {
        	this.setUpClickHandler(position, controller);
        });
        this.gridMap.put(position, pane);        
		return pane;
	}

	@Override
	public void setUpClickHandler(Coordinate position, GameController controller) {
        System.out.printf("Mouse clicked cell " + position.toString() + "\n");
        if (this.selectedBarrier.isEmpty()) {
        	controller.movePlayer(position);
        } else {
        	if(this.selectedBarrier.get().equals(0)) {
        		controller.placeBarrier(position, Orientation.VERTICAL);
        		System.out.printf("Barrier placement request: " + position.toString() + " Orientation: " + Orientation.VERTICAL + "\n");
        		
        	} else {
        		controller.placeBarrier(position, Orientation.HORIZONTAL);            		
        		System.out.printf("Barrier placement request: " + position.toString() + " Orientation: " + Orientation.HORIZONTAL + "\n");
        	}
        	this.selectedBarrier = Optional.empty();
        }
	}
	
	public void setSelectedBarrier(String type) {
		if (type.equals("vertical")) {
			this.selectedBarrier = Optional.of(0);
			this.drawTextLogic("barrier");
		} else if (type.equals("horizontal")) {
			this.selectedBarrier = Optional.of(1);
			this.drawTextLogic("barrier");
		}
	}
	
    public void clearGrid() {
    	this.gridMap.entrySet().forEach(e -> e.getValue().getChildren().remove(0, e.getValue().getChildren().size()));
    }
    
    @Override
    public void drawPowerUps(List<PowerUp> powerUpsAsList) {
		for (PowerUp p : powerUpsAsList) {
			System.out.println(p);
			switch (p.getType()) {
			case PLUS_ONE_MOVE:
				System.out.println("Drawing powerUp Double Move in " + p.getCoordinate());
				ImageView doubleMoveIcon = new ImageView(new Image(this.getClass()
						.getResourceAsStream("/layouts/main/doubleMovePowerUp.png")));
				doubleMoveIcon.setFitHeight(50);
				doubleMoveIcon.setFitWidth(50);
				doubleMoveIcon.setSmooth(true);
				this.gridMap.get(p.getCoordinate()).setCenter(doubleMoveIcon);
				break;
			case PLUS_ONE_BARRIER:
				System.out.println("Drawing powerUp Plus One Barrier in " + p.getCoordinate());
				ImageView plusOneBarrierIcon = new ImageView(new Image(this.getClass()
						.getResourceAsStream("/layouts/main/barrierPowerUp.png")));
				plusOneBarrierIcon.setFitHeight(50);
				plusOneBarrierIcon.setFitWidth(50);
				plusOneBarrierIcon.setSmooth(true);
				this.gridMap.get(p.getCoordinate()).setCenter(plusOneBarrierIcon);
				break;
			default:
				break;
			}
		}
    }
    
    public void drawBarriersOnLoad(List<Barrier> barrierList) {
    	for (Barrier barrier : barrierList) {
    		this.drawBarrierLogic(barrier);
    	}
    }
    
    public BorderPane getPaneByPosition(Coordinate position) {
    	return this.gridMap.get(position);
    }
    
    public void drawBarrierLogic(Barrier barrier) {
    	BorderPane selected = this.gridMap.get(barrier.getCoordinate());
    	Rectangle verticalBarrier = new Rectangle();
    	verticalBarrier.getStyleClass().add("Barrier");
    	verticalBarrier.setFill(Color.ORANGE);
    	Rectangle horizontalBarrier = new Rectangle();
    	horizontalBarrier.getStyleClass().add("Barrier");
    	horizontalBarrier.setFill(Color.ORANGE);
    	if (barrier.getOrientation().equals(Orientation.HORIZONTAL)) {
    		selected.setBottom(horizontalBarrier);
    		BorderPane.setAlignment(horizontalBarrier, Pos.CENTER);
    	} else if (barrier.getOrientation().equals(Orientation.VERTICAL)) {
    		selected.setRight(verticalBarrier);
    		BorderPane.setAlignment(verticalBarrier, Pos.CENTER);
    	}	
    	Platform.runLater(new Runnable() {
    		
    		@Override
    		public void run() {
    			verticalBarrier.setHeight(gridMap.get(new Coordinate(0,0)).getHeight()/10*8);				
    			verticalBarrier.setWidth(gridMap.get(new Coordinate(0,0)).getHeight()/10);				
    			horizontalBarrier.setHeight(gridMap.get(new Coordinate(0,0)).getHeight()/10);				
    			horizontalBarrier.setWidth(gridMap.get(new Coordinate(0,0)).getHeight()/10*8);				
    		}
    		
    	});
    }
    
    public void deletePowerUp(PowerUp p) {
		List<Node> toRemove = this.gridMap.get(p.getCoordinate()).getChildren().stream()
				.filter(e -> e.getClass().equals(ImageView.class))
				.collect(Collectors.toList()); 
		this.gridMap.get(p.getCoordinate()).getChildren().removeAll(toRemove);

    }

	@Override
	public void drawTextLogic(String textToDisplay) {
    	String moveTutorial = "- Benvenuto su Quoridor! \n- Clicca su una casella adiacente alla tua per muovere la pedina\n"
    			+ "- Clicca su una barriera per posizionarla\n"
    			+ "- Puoi saltare l'avversario quando e` di fronte a te\n";
    	String barrierTutorial = "Per posizionare la barriera, clicca la casella dove vuoi posizionarla: \n"
    			+ "- La barriera verticale sara` posizionata a destra e nella cassela in basso\n"
    			+ "- La barriera orizzontale sara` piazzata in basso e nella casella a destra\n";
    	switch(textToDisplay) {
    		case "start" :
    			this.view.drawText(moveTutorial);
    			break;
    		case "barrier" :
    			this.view.drawText(barrierTutorial);		
    			break;
    		default :
    			this.view.drawText(textToDisplay);
    	}		
		
	}


}
