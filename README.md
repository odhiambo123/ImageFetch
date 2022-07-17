# ImageFetch

### Get all the dependencies to add to the build.gradle(app)
- New layout file in the layout folder, for the images  also create a new drawable resource file with a person and username

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
   android:layout_width="match_parent"
   android:layout_height="250dp"
   xmlns:app="http://schemas.android.com/apk/res-auto">

   <ImageView
       android:id="@+id/image_view"
       android:layout_width="match_parent"
       android:layout_height="match_parent"/>
   <View
       android:layout_width="match_parent"
       android:layout_height="40dp"
       android:layout_alignBottom="@id/image_view"
       android:background="@drawable/gradient"/>
   <TextView
       android:id="@+id/text_view_user_name"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:text="username"
       android:layout_alignBottom="@id/image_view"
       android:textColor="@android:color/black"
       android:gravity="center_vertical"
       android:maxLines="1"
       android:ellipsize="end"
       android:textSize="20sp"
       android:drawablePadding="4dp"
       app:drawableStartCompat="@drawable/ic_baseline_person_24"
       android:layout_margin="8dp"

       />


</RelativeLayout>

```

### create a layout for the fragment that contains the recyclerview

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   xmlns:app="http://schemas.android.com/apk/res-auto"
   xmlns:tools="http://schemas.android.com/tools"
   tools:context=".ui.gallery.GalleryFragment"
>

   <androidx.recyclerview.widget.RecyclerView
       android:id="@+id/recycler_view"
       app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
       tools:listitem="@layout/item_unsplash_photo"
       android:layout_width="match_parent"
       android:layout_height="match_parent"/>

   <ProgressBar
       android:id="@+id/progress_bar"
       android:visibility="gone"
       tools:visibility="visible"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:layout_centerInParent="true"/>

   <TextView
   android:id="@+id/text_view_error"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:layout_above="@id/button_retry"
   android:layout_centerHorizontal="true"
   android:text="@string/results_could_not_be_loaded"
   android:visibility="gone"
   tools:visibility="visible"
   />


   <Button
       android:id="@+id/button_retry"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:text="@string/retry"
       android:layout_centerInParent="true"
       android:visibility="gone"
       tools:visibility="visible"/>
  
   <TextView
       android:id="@+id/text_view_empty"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:text="No results found"
       android:layout_centerInParent="true"
       android:visibility="gone"
       tools:visibility="visible"/>
  
</RelativeLayout>

```

### Create the photo data class and add the fields you need from the JSON schema

```
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
   val id: String,
   val description: String,
   val urls: PhotoUrls,
   val user: User,

   ) : Parcelable {

   @Parcelize
   data class PhotoUrls(
       val raw: String,
       val full: String,
       val regular: String,
       val small: String,
       val thumb: String,
   ) : Parcelable

   @Parcelize
   data class User(
       val name: String,
       val username: String,
   ) : Parcelable {
       val atttributionUrl get() = "https://unsplash.com/$username?utm_source=unsplash&utm_source=ImageFetch&utm_medium=referral"
   }

}

```


### Create a package ui
- Inside the ui create another package gallery
- Create a gallery fragment inside here
- Create another package for data
- Move the data class into the data package


- Create the nav graph. the navigation component in the res folder 
  - In the design view, add the galleryFragment to it using the new destination icon
    this will be the main view of your app



- Switch to the Main Activity layout and add the nav host.
  this is because our main activity is just going to be the host of our fragments
- Delete the textView and add fragment container view

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
   xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:tools="http://schemas.android.com/tools"
   xmlns:app="http://schemas.android.com/apk/res-auto"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   tools:context=".MainActivity">

   <androidx.fragment.app.FragmentContainerView
       android:id="@+id/nav_host_fragment_main"
       android:name="androidx.navigation.fragment.NavHostFragment"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       app:defaultNavHost="true"
       app:navGraph="@navigation/nav_grap" />


 </androidx.constraintlayout.widget.ConstraintLayout>
 ```


### Retrofit code:
- Create a package called api
  - Inside the api package create a data class FetchResponse to contain the result of the fetch
- Create another interface class for the api PhotoApi 
  - this is an interface because the retrofit will implement it


```
interface PhotoApi {
   companion object{
       const val BASE_URL = "https://api.unsplash.com/"  // Base URL for the API https://unsplash.com/documentation#search-photos
       const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
   }
   @Headers("Accept-Version: v1", "")
   @GET("search/photos")//https://unsplash.com/documentation#search-photos
   suspend fun searchPhotos(
       @Query("query") query: String,
       @Query("page") page: Int,
       @Query("per_page") perPage: Int
   ): FetchResponse
}

```

- Glide will help us with scheduling the calls in the suspend functions 
- Hilt will help us with injections

- The idea behind dependency injection is that the classes should not be responsible for creating classes they depend on.

  - The dependencies should be passed to the class from the outside
  - Classes less tightly coupled
  - Clases are easier to test
  - Makes it easier to manage code
  - Makes it easier to scope object to the right lifecycle

- Hilt is a library that makes dagger easier to use



### Create a di package for dependency injection
  - Create an object class AppModule inside the di
  - Add the necessary [dependencies](https://developer.android.com/training/dependency-injection/hilt-android#setup)


### Create object AppModule

		
```
@Module // Add this annotation to indicate that this class is a Dagger module.
// The @InstallIn annotation is used to specify the Component class to install this module in.
@InstallIn(SingletonComponent::class)
object AppModule {
   @Provides // Add this annotation to provide objects to the dependencies graph.
   @Singleton // Add this annotation to indicate that this object should be created once and reused.
   fun provideRetrofit(): Retrofit =
       Retrofit.Builder()
           .baseUrl(PhotoApi.BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
   @Provides
   @Singleton
   fun providePhotoApi(retrofit: Retrofit): PhotoApi =
       retrofit.create(PhotoApi::class.java)

   }
//annotation used tell dagger what to provide

```

### Create the repository

```
@Singleton
class PhotoRepository @Inject constructor(private val photApi: PhotoApi) {
   fun searchPhotos(query: String) =
       Pager(
           config = PagingConfig(
               pageSize = 20,
               maxSize = 100,
               enablePlaceholders = false
           )
       ) {
           PhotosPagingSource(photApi, query)
       }.liveData
}

```


### Create the paging source

```
private const val PHOTOS_STARTING_PAGE_INDEX = 1

class PhotosPagingSource(
   private val photoApi: PhotoApi,
   private val query: String
): PagingSource<Int, Photo>() {
   override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {

       val position = params.key ?: PHOTOS_STARTING_PAGE_INDEX
       return try {
           val response = photoApi.searchPhotos(query, position, params.loadSize)
           val photos = response.results
           LoadResult.Page(
               data = photos,
               prevKey = if (position == PHOTOS_STARTING_PAGE_INDEX) null else position - 1,
               nextKey = if (photos.isEmpty()) null else position + 1
           )
       } catch (e: IOException) {

           LoadResult.Error(e)
       }catch (e: HttpException) {

           LoadResult.Error(e)
       }

   }

   override fun getRefreshKey(state: PagingState<Int, Photo>): Int {
       val position = state.anchorPosition ?: PHOTOS_STARTING_PAGE_INDEX
       return position - 1
   }

}

```

### create the viewModel

```
@HiltViewModel
class GalleryViewModel @Inject constructor(
   private val photoRepository: PhotoRepository,
   state: SavedStateHandle
) : ViewModel() {
   private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

   val photos = currentQuery.switchMap { queryString ->
       photoRepository.searchPhotos(queryString).cachedIn(viewModelScope)
   }
   fun searchPhotos(query: String){
       currentQuery.value = query
   }

   companion object {
       private const val CURRENT_QUERY = "current_query"
       private const val DEFAULT_QUERY = "cute"
   }
}

```


### create the adapter

```
class PhotoAdapter : PagingDataAdapter<Photo, PhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
       val binding =
           ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return PhotoViewHolder(binding)
   }

   override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
       val currentItem = getItem(position)

       if (currentItem != null) {
           holder.bind(currentItem)
       }
   }

   class PhotoViewHolder(private val binding: ItemUnsplashPhotoBinding) :
       RecyclerView.ViewHolder(binding.root) {
       fun bind(photo: Photo) {
           binding.apply {
               Glide.with(itemView)
                   .load(photo.urls.regular)
                   .centerCrop()
                   .transition(DrawableTransitionOptions.withCrossFade())
                   .error(R.drawable.ic_baseline_error_24)
                   .into(imageView)

               textViewUserName.text = photo.user.username
           }
       }

   }

   companion object {
       private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
           override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
               return oldItem.id == newItem.id
           }

           override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
               return oldItem == newItem
           }
       }
   }


}
```


### Finish the gallery fragment

```
@AndroidEntryPoint
class GalleryFragment: androidx.fragment.app.Fragment(R.layout.fragment_gallery) {
   private val viewModel by viewModels<GalleryViewModel>()

   private var _binding : FragmentGalleryBinding? = null

   private val binding get() = _binding!!


   override fun onViewCreated(view: View, savedInstanceState: Bundle?){
       super.onViewCreated(view, savedInstanceState)

       _binding = FragmentGalleryBinding.bind(view)

       val adapter = PhotoAdapter()
       binding.apply {
           recyclerView.setHasFixedSize(true)
           recyclerView.adapter = adapter
       }

       viewModel.photos.observe(viewLifecycleOwner){
           adapter.submitData(viewLifecycleOwner.lifecycle, it)

       }
   }

   override fun onDestroyView() {
       super.onDestroyView()
       _binding = null
   }

}

```

### Create footer navigation

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
   android:orientation="vertical"
   android:layout_width="match_parent"
   android:gravity="center_horizontal"
   android:padding="8dp"
   android:layout_height="wrap_content">

   <ProgressBar
       android:id="@+id/progress_bar"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"/>
   <TextView
       android:id="@+id/text_view_error"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:text="Resuslts Could not be loaded"/>
   <Button
       android:id="@+id/button_retry"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:text="Retry" />


</LinearLayout>

```

### Create adapter for the footer layout

```
class PhotoLoadStateAdapter(private val retry: () -> Unit):
   LoadStateAdapter<PhotoLoadStateAdapter.PhotoLoadStateViewHolder>() {
   override fun onCreateViewHolder(
       parent: ViewGroup,
       loadState: LoadState
   ): PhotoLoadStateViewHolder {
       val binding = PhotoLoadStateFooterBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       )
       return PhotoLoadStateViewHolder(binding)
   }

   override fun onBindViewHolder(holder: PhotoLoadStateViewHolder, loadState: LoadState) {
       holder.bind(loadState)
   }

   inner class PhotoLoadStateViewHolder(private val binding: PhotoLoadStateFooterBinding) :
       RecyclerView.ViewHolder(binding.root) {
       init {
           binding.buttonRetry.setOnClickListener {
               retry.invoke()
           }
       }

       fun bind(loadState: LoadState) {
           binding.apply {
               progressBar.isVisible = loadState is LoadState.Loading
               buttonRetry.isVisible = loadState is LoadState.Error
               textViewError.isVisible = loadState is LoadState.Error
               textViewError.text = (loadState as? LoadState.Error)?.error?.message
           }

       }
   }
}

```
### Connect the adapters in the gallery fragment

```
override fun onViewCreated(view: View, savedInstanceState: Bundle?){
   super.onViewCreated(view, savedInstanceState)

   _binding = FragmentGalleryBinding.bind(view)

   val adapter = PhotoAdapter()
   binding.apply {
       recyclerView.setHasFixedSize(true)
       recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
           header = PhotoLoadStateAdapter {
               adapter.retry()
           },
           footer = PhotoLoadStateAdapter {
               adapter.retry()
           }
       )
   }
   
```



### Implement the search functionality

- Create a new android resource file in the res folder menu_gallery 

```
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:app="http://schemas.android.com/apk/res-auto">
   <item
       android:id="@+id/action_search"
       android:icon="@drawable/ic_baseline_search_24"
       android:title="Search"
       app:actionViewClass="android.widget.SearchView"
       app:showAsAction="ifRoom|collapseActionView" />


</menu>
```
Then override onCreateOptionsMenu in the GalleryFragment

### Homework...
- The onCreateOptionsMenu is Depricated!

