package com.example.charlie.practicaandroid;

import android.annotation.TargetApi;
import android.text.method.Touch;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Charlie on 25/01/2017.
 */

public class TouchHandler implements View.OnTouchListener {

    boolean draw=false; //If true, draws a point in the position of your finger each time an event triggers. Debugging purposes.

    //TODO resolver dependencia de nombres, o renombrar
    ImageView p1,p2; //Image of points p1 and p2 of our minimgame. Its names should be Punto and Punto2

    float lkx,lky; //to check previous movement
    float ox,oy; //origin coordinates (p1)
    float dx,dy; //destiny coordinates (p2)
    float error; //error on fingertips
    float alpha; //alpha in [0,1], checks optimality of the descent (1: optimality)
    boolean path=false; //If the events are a possible solution to the minigame
    boolean checking=false; //enable minigame
    boolean debug=true; //enable debugging output

    public TouchHandler(){
        error=100f;
        alpha=0.5f;
    }

    public TouchHandler(float alpha,float error){
        this.alpha=alpha;
        this.error=error;
    }

    @Override
    @TargetApi(19) //TODO borrar
    public boolean onTouch (View v, MotionEvent event){
        if(checking) {
            float x = event.getX(), y = event.getY();

            if (draw) {
                // RelativeLayout. though you can use xml RelativeLayout here too by `findViewById()`
                RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.activity_main);
                // ImageView
                ImageView imageView = new ImageView(v.getContext());

                // Setting layout params to our RelativeLayout
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                // Setting position of our ImageView
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                imageView.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_000);
                int h = imageView.getHeight();
                int w = imageView.getWidth();
                layoutParams.leftMargin = (int) (event.getX());
                layoutParams.topMargin = (int) (event.getY());
                imageView.setLayoutParams(layoutParams);
                Log.d("Adding point in ", "" + layoutParams.leftMargin + " " + layoutParams.topMargin);
                // Finally Adding the imageView to RelativeLayout and its position
                //relativeLayout.addView(imageView, layoutParams);
                ((ViewGroup) v).addView(imageView);
            }

            //TODO Pasar a constructor?
            if (p1 == null) { //Gets the points to a correct state the first time it triggers
                p1 = (ImageView) v.findViewById(R.id.Point);
                p2 = (ImageView) v.findViewById(R.id.Point2);
                ox = p1.getRight();
                oy = p1.getTop();
                dx = p2.getRight();
                dy = p2.getTop();
                Log.d("Points", "Origin: " + ox + " " + oy);
                Log.d("Points", "Destiny: " + dx + " " + dy);
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getPointerCount() == 1) {
                    lkx = x;
                    lky = y;
                }
                if (distance(x, y, ox, oy) < error) {
                    path = true;
                    Log.d("PRESS_EVENT", "P1 pulsado");
                } else {
                    path = false;
                    if (distance(x, y, dx, dy) < error) {
                        Log.d("PRESS_EVENT", "P2 pulsado");
                    }
                }
                Log.d("PRESS_EVENT", "" + event.getX() + " " + event.getY());
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (path) {
                    if (distance(x, y, dx, dy) < error) {
                        path = false;
                        Log.d("PATH_EVENT", "Llegada a P2");
                    }
                    if ((distance(lkx, lky, dx, dy) - distance(x, y, dx, dy)) < distance(lkx, lky, x, y) * alpha) {
                        path = false;
                        Log.d("PATH_EVENT", "Fin de camino: Distance A:" + distance(x, y, dx, dy) + ", LK:" + distance(lkx, lky, dx, dy) + ", DIFF:" + (distance(lkx, lky, dx, dy) - distance(x, y, dx, dy)) + ",TOL:" + distance(lkx, lky, x, y) * alpha);

                    }
                    lkx = x;
                    lky = y;
                }

                if (path) {
                    Log.d("MOVE_EVENT", "Position: " + event.getX() + " " + event.getY() + ". Distance: " + distance(x, y, dx, dy));

                }

            }
            //Log.d("DATA: ",""+MotionEvent.actionToString(event.getAction()));
            return true; //true to be able to capture move events after a down event
        }else{
            Log.d("TOUCH LISTENER","STATUS: DISCONNECTED");
            return false;
        }

    }
    //TODO viscoso pero sabroso
    private float distance(float x1,float y1, float x2, float y2){
        return (float) Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) );
    }
    public void setError(float e){
        this.error=e;
    }

    public void startChecking(){
        checking=true;
    }
    public void stopChecking(){
        checking=false;
        path=false;
    }
}
