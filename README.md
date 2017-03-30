# DrawCircle

We can draw circle on touching the screen and able to adjust the radius of circle.

DEMO

![Alt text](https://github.com/MobileDews/DrawCircle/blob/master/drawcircle.png?raw=true "Optional Title")

GRADLE

Step 1. Add the JitPack repository to your root build.gradle

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

    dependencies {
	        compile 'com.github.MobileDews:DrawCircle:1.0.1'
	  }
   

USAGE

       <com.techdew.lib.CircleTouchView
        android:id="@+id/circle_drawer_view"
        android:layout_centerInParent="true"
        custom:circle_color="@color/colorAccent"
        custom:stroke_color="@color/colorPrimary"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
        
Add the following code to the MainActivity.java

    public class MainActivity extends AppCompatActivity  {
    CircleTouchView circleTouchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleTouchView = (CircleTouchView) findViewById(R.id.circle_drawer_view);
        circleTouchView.setEventListener(new OnCircleTouchListener() {
            @Override
            public void circleValue(float radius) {
                Log.d("circleValue","===>"+radius);
            }
        });

    }

    }
        
