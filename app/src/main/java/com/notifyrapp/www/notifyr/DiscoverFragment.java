package com.notifyrapp.www.notifyr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.notifyrapp.www.notifyr.Business.Business;
import com.notifyrapp.www.notifyr.Business.CacheManager;
import com.notifyrapp.www.notifyr.Business.CallbackInterface;
import com.notifyrapp.www.notifyr.Model.Item;
import com.notifyrapp.www.notifyr.UI.DiscoverRecyclerAdapter;
import com.notifyrapp.www.notifyr.UI.InfiniteScrollListener;
import com.notifyrapp.www.notifyr.UI.PopularItemAdapter;
import com.squareup.picasso.Cache;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscoverFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mSuggestedAdapter;
    private PopularItemAdapter mPopularAdapter;
    private int currentPage = 0;
    private final int pageSize = 20;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";
    private TextView topResultsTextView;
    private Context ctx;
    private List<Item> suggestedItemsList;
    private List<Item> popularItemsList;
    private ListView mPopularListView;
    private OnFragmentInteractionListener mListener;
    private InfiniteScrollListener mInfiniteScrollListener;
    private SwipeRefreshLayout swipeSuggested;
    private ProgressBar pbFooter;
    private boolean footerRemoved = false;
    private View footerViewInitLoad;
    private View footerViewLoadMore;
    private View suggestedProgressView;
    private ProgressBar pbSuggested;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //((MainActivity)getActivity()).getSupportActionBar().hide();
        final View view = inflater.inflate(R.layout.fragment_discover, container, false);
        final Business business = new Business(getContext());
        this.ctx = getContext();
        footerRemoved = false;
        ((MainActivity)getActivity()).btnEditDone.setVisibility(View.GONE);
        ((MainActivity)getActivity()).btnTrashCanDelete.setVisibility(View.GONE);
        ((MainActivity)getActivity()).abTitle.setText(R.string.menu_tab_2);
        View progressView = inflater.inflate(R.layout.progress_circle, null);

        footerViewInitLoad = ((LayoutInflater) this.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.progress_circle_padded, null, false);
        footerViewLoadMore = ((LayoutInflater) this.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.progress_circle, null, false);
        pbFooter = (ProgressBar) progressView.findViewById(R.id.pb_main);
        pbFooter.setVisibility(View.VISIBLE);

        // Popular Items and Main List View
        View header = inflater.inflate(R.layout.list_item_discover_header, null);
        mPopularListView = (ListView) view.findViewById(R.id.popular_list_view);
        //mPopularListView.setHeaderDividersEnabled(false);
        mPopularListView.addHeaderView(header);
        mPopularListView.addFooterView(footerViewInitLoad);
        popularItemsList = new ArrayList<Item>();
        mPopularAdapter = new PopularItemAdapter(ctx, popularItemsList);
        mPopularListView.setAdapter(mPopularAdapter);

        // Add the scroll listener to know when we hit the bottom
        mInfiniteScrollListener = new InfiniteScrollListener(2) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                if(totalItemsCount > 10) {
                    pbFooter.setVisibility(View.VISIBLE);
                    getPopularItems((currentPage+1) * pageSize, pageSize,true);
                }
            }

            @Override
            public void onUpScrolling() {
            }

            @Override
            public void onDownScrolling() {
            }

        };
        mInfiniteScrollListener.setCurrentPage(0);
        mPopularListView.setOnScrollListener(mInfiniteScrollListener);

        //Define your Searchview
        final SearchView searchView = (SearchView) view.findViewById(R.id.search_view);
        topResultsTextView = (TextView) view.findViewById(R.id.txtSettings);

        swipeSuggested = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutDiscoverRecyclerview);
        swipeSuggested.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSuggestedItems(true);
            }
        });
        swipeSuggested.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(searchView.isIconified())
                {
                  //  ((MainActivity)getActivity()).getSupportActionBar().setShowHideAnimationEnabled(true);
                  //  ((MainActivity)getActivity()).getSupportActionBar().hide();
                }
                searchView.setIconified(false);
            }
        });

        /*searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(!searchView.isIconified())
                {
                    ((MainActivity)getActivity()).getSupportActionBar().setShowHideAnimationEnabled(true);
                    ((MainActivity)getActivity()).getSupportActionBar().show();
                }
                return false;
            }
        });*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // your text view here
                if((newText!= null && newText.equals("")) || newText.length() <= 1) {

                    topResultsTextView.setText(R.string.header_you_might_like);
                    suggestedItemsList.clear();
                    getSuggestedItems(false);
                }
                else {
                    DiscoverRecyclerAdapter.noResultsFound = false;
                    mRecyclerView.setAdapter(mSuggestedAdapter);
                    topResultsTextView.setText("Results...");
                    suggestedItemsList.clear();
                    if(newText.length() > 2) {
                 /*       List<Item> results = business.getLocalItemsByQuery(newText);
                        suggestedItemsList.clear();
                        suggestedItemsList.addAll(results);
                        if(results.size() == 0)
                        {
                            DiscoverRecyclerAdapter.noResultsFound = true;
                            mRecyclerView.setAdapter(mSuggestedAdapter);
                        }

                        mSuggestedAdapter.notifyDataSetChanged();*/

                        business.getItemsByQuery(newText, new CallbackInterface() {
                            @Override
                            public void onCompleted(Object data) {
                                suggestedItemsList.clear();
                                List<Item> results = (List<Item>) data;
                                suggestedItemsList.addAll(results);
                                if(results.size() == 0)
                                {
                                    DiscoverRecyclerAdapter.noResultsFound = true;
                                    mRecyclerView.setAdapter(mSuggestedAdapter);
                                }

                                mSuggestedAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                //topResultsTextView.setText(R.string.header_you_might_like);
                return true;
            }
        });

        // Suggested Items
        mRecyclerView = (RecyclerView) view.findViewById(R.id.discover_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);//new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        suggestedItemsList = new ArrayList<Item>();
        mSuggestedAdapter = new DiscoverRecyclerAdapter(suggestedItemsList);
        mRecyclerView.setAdapter(mSuggestedAdapter);
        getSuggestedItems(false);
        getPopularItems(0,pageSize,false);

        return view;
    }

    private void getSuggestedItems(Boolean forceServerLoad)
    {
        // First check if popular items is cached else get it from server
        List<Item> cachedItems = (List<Item>)CacheManager.getObjectFromMemoryCache("suggested_items");
        if(cachedItems != null && forceServerLoad == false)
        {
            suggestedItemsList.addAll(cachedItems);
            swipeSuggested.setRefreshing(false);
            mSuggestedAdapter.notifyDataSetChanged();
        }
        else
        {
            new Business(ctx).getPopularItemsByItemTypeIdFromServer(0, 40,-1, new CallbackInterface() {
                @Override
                public void onCompleted(Object data) {
                    List<Item> downloadedItems = (List<Item>) data;
                    CacheManager.deleteObjectFromCache("suggested_items");
                    CacheManager.saveObjectToMemoryCache("suggested_items", data);
                    suggestedItemsList.addAll(downloadedItems);
                    swipeSuggested.setRefreshing(false);
                    if(downloadedItems.size() == 0)
                    {
                        DiscoverRecyclerAdapter.noResultsFound = true;
                    }
                    else
                    {
                        DiscoverRecyclerAdapter.noResultsFound = false;
                    }
                    mSuggestedAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void getPopularItems(final int skip, int take, final Boolean forceServerLoad)
    {
        if(pbFooter != null)  pbFooter.setVisibility(View.VISIBLE);
        // First check if popular items is cached else get it from server
        List<Item> cachedItems = (List<Item>)CacheManager.getObjectFromMemoryCache("popular_items");
        if(cachedItems != null && !forceServerLoad)
        {
            popularItemsList.addAll(cachedItems);
            mSuggestedAdapter.notifyDataSetChanged();
        }
        else
        {
            new Business(ctx).getPopularItemsFromServer(skip, take, new CallbackInterface() {
                @Override
                public void onCompleted(Object data) {
                    List<Item> downloadedItems = (List<Item>) data;

                    if(skip == 0) {
                        CacheManager.deleteObjectFromCache("popular_items");
                        CacheManager.saveObjectToMemoryCache("popular_items", data);
                    }
                    popularItemsList.addAll(downloadedItems);
                    currentPage++;
                    mPopularAdapter.notifyDataSetChanged();
                    if(downloadedItems.size() == 0)
                    {
                        if(pbFooter != null)  pbFooter.setVisibility(View.GONE);
                    }

                    // Need to swap the padded loading icon with the load more icon
                    if(footerRemoved == false) {
                        mPopularListView.removeFooterView(footerViewInitLoad);
                        mPopularListView.addFooterView(footerViewLoadMore);
                        footerRemoved = true;
                    }

                }
            });
        }
        if(pbFooter != null)  pbFooter.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
