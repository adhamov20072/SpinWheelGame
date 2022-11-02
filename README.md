# SpinWheelGame

Android custom component that displays a lucky wheel. it features easy customize of colors , text and addition of items and it's very trivial to integrate in your application.

# What's new in 0.3.0

 **Sample**
 
 ![Lucky wheel](https://static.vecteezy.com/system/resources/previews/002/879/010/non_2x/wheel-of-fortune-lucky-icon-illustration-free-vector.jpg). 

**Overview**

The LuckyWheel shows a wheel which can spin, and a marker that indicates the selected item in the wheel. Items may be customized with text, image, or a color.

The wheel starts off stationary. Calling the `setTarget()` method with an integer parameter will set the wheel to spin to that section when the user taps the wheel in the UI. Calling `rotateWheelTo()` with an integer parameter will cause the wheel to spin to that section immediately.

The wheel spins for a few seconds, then fires the `setLuckyWheelReachTheTarget` callback to notify the application it has stopped moving.


 **Installing**
 
 Add it in your root build.gradle at the end of repositories:
 
  ```groovy
 	allprojects {
 		repositories {
 			...
 			maven { url 'https://jitpack.io' }
 		}
 	}
  ```
 	
 Add the dependency
 
  ```groovy
  dependencies {
 		 implementation 'com.github.adhamov20072:SpinWheelGame:1.0.1'
 	}
  ```
 	
 **XML**
 
 RelativeLayout
 
```xml
<com.aimardon.spinname.LuckyWheel
        android:id="@+id/lwv"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerInParent="true"
        LuckyWheel:background_color="@color/colorPrimary" />

    <Button
        android:id="@+id/start"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_margin="10dp"
        android:background="#00E6FF"
        android:textColor="@android:color/white"
        android:text="start"/>
```


 **Kotlin**
 
 
 ```kotlin MainActivity
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val exampleThread by lazy {
        ExampleThread()
    }
    var wheelItems: MutableList<WheelItem?>? = null
    val handler=Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        generateWheelItems()
        binding.lwv.addWheelItems(wheelItems!!)
        exampleThread.start()
    }
    inner class ExampleThread:Thread(){
        override fun run() {
            handler.post {
                var a: Int
                binding.start.setOnClickListener {
                    a = Random.nextInt(wheelItems!!.size)
                    binding.lwv.setTarget(a)
                    binding.lwv.rotateWheelTo(a, 200)
                binding.start.isClickable=false
                }
                    binding.lwv.setLuckyWheelReachTheTarget(object : OnLuckyWheelReachTheTarget {
                        override fun onReachTarget() {
                            binding.start.isClickable=true
                        }
                    })
            }
        }
    }
    private fun generateWheelItems() {
        wheelItems = ArrayList()
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#fc6c6c"), "100 $"
            )
        )
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#00E6FF"), "0 $"
            )
        )
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#F00E6F"), "30 $"
            )
        )
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#00E6FF"), "6000 $"
            )
        )
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#fc6c6c"), "9 $"
            )
        )
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#00E6FF"), "20 $"
            )
        )
    }
}
 ```
 
 * Add sections to wheel  
 
 ```kotlin
  addWheelItems(wheelItems)
 ``` 
 * Rotate by touch -- set target before user touch wheel 
 
 ```kotlin
  setTarget(3)
 ``` 
 * Rotate to section and Animation Time
 
 ```koltin
  rotateWheelTo(Random.nextInt(wheelItems.size),2000)
 ``` 
 * On target reach listener
 
 ```koltin
  lwv.setLuckyWheelReachTheTarget(object : OnLuckyWheelReachTheTarget {
  override fun onReachTarget() {
      }
  })
 ```
