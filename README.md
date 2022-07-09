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
- Install firebase-crashlytics, firebase-analytics, firebase-config (optional)
- Install RxJava/RxKotlin

# Usage
- Just extend the `AppBaseActivity` when creating an activity. It has build-in functions to keep the amount of boilerplate code to a minimum and handles known-issues.
```
class MainActivity : AppBaseActivity<MainActivityViewBinding>() {

  //it manages the lifecycle of viewbinding and avoid memory leak
  override fun onCreateViewBinding(layoutInflater: LayoutInflater): MainActivityViewBinding =
        MainActivityViewBinding.inflate(layoutInflater)
  
  override fun setupView(savedInstanceState: Bundle?) {
    //binding is your ViewBinding
    binding.yourView
  }
}
```
- Or if you want to create a Fragment, change the `AppBaseActivity` above to `AppBaseDialogFragment` or `AppBaseFragment`, the types are different but the interface is similar

- To create an `RecyclerAdapter`:
```
class YourAdapter(list: MutableList<YourData>) :
    AppBaseAdapter<YourData, YourViewBinding>(list) {
    override fun onCreateViewBinding(inflater: LayoutInflater, viewType: Int): YourViewBinding =
        YourViewBinding.inflate(inflater)

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
//handle onClickListenr:
val adapter = YourAdapter(...)
adapter.registerItemClickListener().observe(this) {item->
  //handle onClickListener
}
```
