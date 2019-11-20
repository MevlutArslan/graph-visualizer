package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {

    Node start;
    Node end;

    public Canvas mainCanvas;
    public Button btn;
    public Text drawingText;
    public Button bfs_btn;
    public Button start_btn;
    public Button end_btn;


    @FXML
    void run(){

    }
    @FXML
    void drawNode(){
        mainCanvas.setOnMouseClicked(e -> {
            new Node(mainCanvas.getGraphicsContext2D(),e.getX(),e.getY());
        });
        drawingText.setText("Currently drawing : ");
        drawingText.setText(drawingText.getText() + " node" );
    }

    @FXML
    void drawLine(){
        GraphicsContext ctx = mainCanvas.getGraphicsContext2D();
        mainCanvas.setOnMouseClicked(e->{
            boolean isSelected = false;
            Node currentNode = null;
            for( Node x : Node.nodes){
                ctx.setFill(Color.LIGHTBLUE);
                ctx.fillOval(x.xCord-x.RADIUS,x.yCord-x.RADIUS,x.DIAMETER,x.DIAMETER);
                //checking mouse location and comparing it to all the nodes in the canvas
                if((e.getX() >= x.xCord - x.RADIUS && e.getX() <= x.xCord)&& (e.getY() >= x.yCord - x.RADIUS&& e.getY() <= x.yCord)) {
                    //if the mouse is clicked at the location of a node we mark it as currentNode and then redraw it in RED to indicate its chosen
                    isSelected = true;
                    currentNode = x;
                    x.reDraw(ctx,Color.RED);
                }
            }
                //after making sure a node is selected then we use the currentNode to draw a line from its center to the second selected Node
                if(isSelected){
                    Node finalCurrentNode = currentNode;
                    mainCanvas.setOnMouseClicked(ev->{
                        double[] toDraw = new double[4];
                        for(Node x : Node.nodes){
                            if((ev.getX() >= x.xCord - x.RADIUS && ev.getX() <= x.xCord)&& (ev.getY() >= x.yCord - x.RADIUS&& ev.getY() <= x.yCord)){
                                ctx.strokeLine(finalCurrentNode.xCord, finalCurrentNode.yCord,x.xCord,x.yCord);
                                finalCurrentNode.connectedNodes.add(x);
                                x.connectedNodes.add(finalCurrentNode);
                            }
                        }
                    });
                }
            });

        drawingText.setText("Currently drawing : ");
        drawingText.setText(drawingText.getText() + " line" );
    }

    @FXML
    void clearCanvas(){
        Node.nodes.clear();
        for(Node x : Node.nodes){
            x.connectedNodes.clear();
        }
        mainCanvas.setOnMouseClicked(e->{});
        mainCanvas.getGraphicsContext2D().clearRect(0,0,mainCanvas.getWidth(),mainCanvas.getHeight());

        drawingText.setText("Currently drawing : ");
        drawingText.setText(drawingText.getText() + " nothing" );
    }

    @FXML
    void selectStart(){
        mainCanvas.setOnMouseClicked(e->{});
        GraphicsContext ctx = mainCanvas.getGraphicsContext2D();
        mainCanvas.setOnMouseClicked(e ->{
            for(Node x : Node.nodes){
                if((e.getX() >= x.xCord - x.RADIUS && e.getX() <= x.xCord)&& (e.getY() >= x.yCord - x.RADIUS&& e.getY() <= x.yCord)){
                    start = x;
                    start_btn.setDisable(true);
                    x.reDraw(ctx,Color.PURPLE);
            }}
        });

    }

    @FXML
    void selectEnd(){
        mainCanvas.setOnMouseClicked(e->{});
        GraphicsContext ctx = mainCanvas.getGraphicsContext2D();
        mainCanvas.setOnMouseClicked(e ->{
            for(Node x : Node.nodes){
                if((e.getX() >= x.xCord - x.RADIUS && e.getX() <= x.xCord)&& (e.getY() >= x.yCord - x.RADIUS&& e.getY() <= x.yCord)){
                    end = x;
                    end_btn.setDisable(true);
                    x.reDraw(ctx,Color.GREEN);
                }}
        });
    }

    @FXML
    void bfs(){
        PriorityQueue<Node> queue = new PriorityQueue<>();
        int counter = 0;
        ArrayList<Node> notVisitedNodes = new ArrayList<>();

        for(Node x : Node.nodes){
            if(x.isVisited == false) notVisitedNodes.add(x);
        }

        start.setVisited(true);
        queue.offer(start);
        while (queue.size() > 0){
            Node curr = queue.poll(); //gets the 1st element from que;

            //end condition
            if(curr == end){
                break;
            }
            for(Node x : curr.connectedNodes){
               if(x.isVisited() == false){
                   x.setVisited(true);
                   x.parent = curr;
                   queue.offer(x);
                   counter ++;
               }
            }
        }

        ArrayList<Node> path = new ArrayList<>();
        path.add(end);
        Node next = end.parent;
        while(next != null){
            path.add(next);
            next= next.parent;
        }

        GraphicsContext ctx = mainCanvas.getGraphicsContext2D();
        for(Node x : path){
            x.reDraw(ctx,Color.ORANGE);
        }
    }
}


