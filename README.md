# android_codebase
Support for quickly building android apps by providing useful functions and optimized MVVM architecture.

# Installation:
## Install this library:
Add it in your root build.gradle at the end of repositories:
```
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
```

Step 2. Add the dependency, latest_version is [![](https://jitpack.io/v/lokile/android_codebase.svg)](https://jitpack.io/#lokile/android_codebase)
```
dependencies {
  implementation 'com.github.lokile:android_codebase:latest_version'
}
```
## Install required libraries:
- Enable mutidex
- Enable viewBinding:
```
android{
    ...
    buildFeatures {
        viewBinding true
    }
    ...
}
```
- Ktx:
```
  implementation "androidx.core:core-ktx:1.8.0"
  implementation "androidx.fragment:fragment-ktx:1.5.0"
```
- Install firebase-crashlytics, firebase-analytics, firebase-config (optional)
- Install RxJava/RxKotlin

# Usage
## Base Actvity/Fragment:
- Extend the `AppBaseActivity`, `AppBaseDialogFragment`, `AppBaseFragment` when creating activities or fragments. It has built-in functions to keep the amount of boilerplate code to a minimum and handles known issues for you.
```
class MainActivity : AppBaseActivity() {

  /*
    - It manages the lifecycle of viewbinding and avoid memory leak
    - No need to call setContentView(), it will do it for you
  */
  val binding by viewBinding(YourActivityBinding::inflate)
  
  override fun setupView(savedInstanceState: Bundle?) {
    binding.yourView
  }
}
```
```
class YourFragment : AppBaseFragment() {

  /*
    - It manages the lifecycle of viewbinding and avoid memory leak
    - No need to handle onCreateView(), it will do it for you
  */
  val binding by viewBinding(YourFragmentBinding::inflate)
  
  override fun setupView(savedInstanceState: Bundle?) {
    binding.yourView
  }
}
```
## Base Adapter:
- To create an `RecyclerAdapter`:
```
class YourAdapter(list: MutableList<YourData>) : AppBaseAdapter<YourData, YourViewBinding>(list) {
    override fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): YourViewBinding =
        YourViewBinding.inflate(inflater, parent, false)
    
    override fun onBindView(
        context: Context,
        holder: AppBaseRecyclerViewHolder<YourViewBinding>,
        position: Int,
        item: YourData
    ) {
        //update your view here
    }
}
/*************/

val adapter = YourAdapter(...)
adapter.registerItemClickListener().observe(this) {item->
  //handle onClickListener
} 
```

## Send/Receive events between modules:
- Send event (can call this function anywhere)
```
broadcastEvent(yourEventTypeData)
```

- Receive:
```
/* 
  this functions is supported in the following components:
  - AppBaseActivity
  - AppBaseDiLogFragment
  - AppBaseFragment
  - AppBaseViewModel
  - BaseApplication
  
*/
registerEventListener<YourEventType> {event->

}
```
