package com.example.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frontend.model.Product;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomProductsAdapter extends ArrayAdapter<Product> {

    private ArrayList<Product> productArrayList;
    private Context mContext;

    // View lookup cache
    private static class ProductViewHolder {
        TextView textViewProductName;
       // ImageView imageViewProduct;

        public ProductViewHolder(TextView textViewProductName){
            this.textViewProductName = textViewProductName;
        }
    }

    public CustomProductsAdapter(ArrayList<Product> productArrayList, Context context) {
        super(context, R.layout.product_row_item, productArrayList);
        this.productArrayList = productArrayList;
        this.mContext=context;
    }

    // OnClick pour traiter l'image
    /*@Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Product product = (Product) object;

        switch (v.getId())
        {
            case R.id.item_info:
                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }*/

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProductViewHolder productViewHolder; // view lookup cache stored in tag

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.product_row_item, parent, false);

            TextView textViewProductName = convertView.findViewById(R.id.textViewProductName);
            //imageViewProduct = (ImageView) convertView.findViewById(R.id.imageViewProduct);

            convertView.setTag(new ProductViewHolder(textViewProductName));

        }
       else {
            productViewHolder = (ProductViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        Product product = getItem(position);

        productViewHolder = (ProductViewHolder) convertView.getTag();
        productViewHolder.textViewProductName.setText(product.getName());

        return convertView;

        /*Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;*/
    }

}
