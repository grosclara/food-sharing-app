package com.example.cshare.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cshare.Controllers.Fragments.AddProductFragment;
import com.example.cshare.R;

import butterknife.ButterKnife;

public class AddProductActivity extends AppCompatActivity implements
        AddProductFragment.OnButtonClickedListener{

    // FOR FRAGMENTS
    // Declare fragment handled by Navigation Drawer
    private Fragment fragmentAddProduct;

    // FOR DATAS
    // Identify each fragment with a number
    private static final int FRAGMENT_ADD_PRODUCT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Binding views
        ButterKnife.bind(this);

        // Show First Fragment
        this.showFragment(FRAGMENT_ADD_PRODUCT);
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show fragment according an Identifier
    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case FRAGMENT_ADD_PRODUCT:
                this.showAddProductFragment();
                break;
            default:
                break;
        }
    }

    private void showAddProductFragment() {
        if (this.fragmentAddProduct == null) this.fragmentAddProduct = new AddProductFragment();
        this.startTransactionFragment(this.fragmentAddProduct);
    }

    // Generic method that will replace and show a fragment inside the HomeActivity Frame Layout
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            // Get our FragmentManager & FragmentTransaction (Inside an activity)
            // Use of SupportFragmentManager instead of FragmentManager to support
            // Android versions lower than 3.0
            getSupportFragmentManager().beginTransaction()
                    // Add it to FrameLayout container
                    .replace(R.id.activity_add_product_frame_layout, fragment).commit();
        }
    }

    // --------------
    // CallBack
    // --------------

    @Override
    public void onButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
