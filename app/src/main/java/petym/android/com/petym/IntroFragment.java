package petym.android.com.petym;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import petym.android.com.petym.VO.DateItemVO;
import petym.android.com.petym.aLogin.Common;

public class IntroFragment extends Fragment {
    private List<Intro> introList;
    private final static String TAG = "IntroFragment";
    private ArrayList<DateItemVO> dateItems,dateItemsImage;
    private AsyncTask dateItemTask;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;



    public IntroFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class DateItemTask extends AsyncTask<String, Void, ArrayList<DateItemVO>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected ArrayList<DateItemVO> doInBackground(String... params) {
            String url = params[0];
            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dateItem", "dateItem");
            try {
                jsonIn = getRemoteData(url, jsonObject.toString());
                Log.d(TAG, "jsonin=" + jsonIn);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return null;
            }

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<DateItemVO>>() {
            }.getType();

            return gson.fromJson(jsonIn, listType);
        }


        @Override
        protected void onPostExecute(ArrayList<DateItemVO> dateItems) {
            super.onPostExecute(dateItems);
//            IntroFragment.this.dateItems = dateItems;

            progressDialog.cancel();

//            Bundle bundle = new Bundle();
//            bundle.putParcelableArrayList("dateItems",dateItems);
//            IntroFragment fragmentA = new IntroFragment();
//            fragmentA.setArguments(bundle);
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(R.id.recyclerView1, fragmentA, TAG);
//            transaction.commit();
        }
    }
    private class DateItemImageTask extends AsyncTask<Object, Integer, Bitmap> {
        private final static String TAG = "DateItemImageTask";
        private final static String ACTION = "getImage";
        private final WeakReference<ImageView> imageViewWeakReference;
        private final DateItemVO dateItemVO;


        DateItemImageTask(ImageView imageView,DateItemVO dateItemVO) {
            this.imageViewWeakReference = new WeakReference<>(imageView);
            this.dateItemVO = dateItemVO;
        }
        @Override
        protected Bitmap doInBackground(Object... params) {
            String url = params[0].toString();
            int id = Integer.parseInt(params[1].toString());
            int imageSize = Integer.parseInt(params[2].toString());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dateItem", ACTION);
            jsonObject.addProperty("id", id);
            jsonObject.addProperty("imageSize", imageSize);

            Bitmap bitmap;
            try {
                bitmap = getRemoteImage(url, jsonObject.toString());
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return null;
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.drawable.default_image);
                }
            }
            super.onPostExecute(bitmap);
        }

        private Bitmap getRemoteImage(String url, String jsonOut) throws IOException {
            Bitmap bitmap = null;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true); // allow inputs
            connection.setDoOutput(true); // allow outputs
            connection.setUseCaches(false); // do not use a cached copy
            connection.setRequestMethod("POST");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(jsonOut);
            Log.d(TAG, "jsonOut: " + jsonOut);
            bw.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
            connection.disconnect();
            return bitmap;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (networkConnected()) {
            try {
                dateItems = new DateItemTask().execute(Common.URL+"MainActivity").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }
        Log.d(TAG, "dateItems:" + dateItems.size() + "" + dateItems);
        View view = inflater.inflate(R.layout.intro_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerView2);
        RecyclerView recyclerView3 = (RecyclerView) view.findViewById(R.id.recyclerView3);
        RecyclerView recyclerView4 = (RecyclerView) view.findViewById(R.id.recyclerView4);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView2.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView3.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView4.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(new IntroAdapter(inflater));
        recyclerView2.setAdapter(new IntroAdapter(inflater));
        recyclerView3.setAdapter(new IntroAdapter(inflater));
        recyclerView4.setAdapter(new IntroAdapter(inflater));

//        IntroFragment.this.inflater = inflater;
//        IntroFragment.this.container = container;
//        IntroFragment.this.savedInstanceState = savedInstanceState;


        return view;

    }

    private class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.ViewHolder> {
        private LayoutInflater inflater;

        public IntroAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvTitle,tvlocal;
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                tvlocal = (TextView) itemView.findViewById(R.id.tvlocal);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.intro_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final DateItemVO dateItemVO = dateItems.get(position);
            int id = dateItemVO.getDateItemNo();
            int imageSize = 450;

            viewHolder.tvName.setText(dateItemVO.getDateItemText().toString());
            viewHolder.tvTitle.setText(dateItemVO.getDateItemTitle().toString());
            viewHolder.tvlocal.setText(dateItemVO.getDateItemLocate().toString());
            if (networkConnected()) {
                try {
                    new DateItemImageTask(viewHolder.imageView,dateItemVO).execute(Common.URL+"MainActivity",id, imageSize);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(),"cool",Toast.LENGTH_SHORT);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dateItemVO", dateItemVO);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dateItems.size();
        }

        private class DateItemTaskImage {
        }
    }


    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut: " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonIn.append(line);
            }
        } else {
            Log.d(TAG, "response code: " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonInqq: " + jsonIn);
        return jsonIn.toString();
    }

    private void showToast(IntroFragment introFragment, int messageId) {
        Toast.makeText(introFragment.getActivity(), messageId, Toast.LENGTH_SHORT).show();
    }


}