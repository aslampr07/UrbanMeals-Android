package com.urbanmeals.client.urbanmeals.home.fragments.profile


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.urbanmeals.client.urbanmeals.R
import com.urbanmeals.client.urbanmeals.activities.EditProfileActivity
import org.w3c.dom.Text


class ProfileFragment : Fragment(), ProfilePresenter.Contract {


    private var token: String? = null

    private lateinit var editProfileButton: Button
    private lateinit var nameView: TextView
    private lateinit var photoCountView: TextView
    private lateinit var reviewCountView: TextView
    private lateinit var badge: ImageView
    private lateinit var aboutLabel: TextView
    private lateinit var bioView: TextView
    private lateinit var dpView: SimpleDraweeView
    private lateinit var photoRecyler: RecyclerView

    private lateinit var presenter: ProfilePresenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_profile, container, false)

        val preferences = this.activity?.getSharedPreferences("credentials", Context.MODE_PRIVATE)
        token = preferences?.getString("token", "")

        presenter = ProfilePresenter(token, context, this)

        editProfileButton = view.findViewById(R.id.Profile_EditProfileButton)
        nameView = view.findViewById(R.id.Profile_ProfileNameView)
        photoCountView = view.findViewById(R.id.Profile_PhotoCountView)
        reviewCountView = view.findViewById(R.id.Profile_ReviewCountView)
        badge = view.findViewById(R.id.Profile_VerifiedBloggerBadge)
        bioView = view.findViewById(R.id.Profile_BioView)
        aboutLabel = view.findViewById(R.id.Profile_AboutName)
        dpView = view.findViewById(R.id.Profile_DPViewer)
        photoRecyler = view.findViewById(R.id.Profile_PhotoViewerRecycler)

        editProfileButton.setOnClickListener {
            val i = Intent(context, EditProfileActivity::class.java)
            startActivity(i)
        }

        photoRecyler.layoutManager = GridLayoutManager(context, 2)

        presenter.getProfile()

        return view
    }

    override fun onLabelSuccess(name: String, photoCount: String, reviewCount: String) {
        nameView.text = name
        photoCountView.text = photoCount
        reviewCountView.text = reviewCount
    }

    override fun isBlogger(blogger: Boolean) {
        if(blogger){
            badge.visibility = View.VISIBLE
        }
    }

    override fun onBioSuccess(name: String, bio: String) {
        aboutLabel.visibility = View.VISIBLE
        bioView.visibility = View.VISIBLE

        aboutLabel.text = name
        bioView.text = bio
    }

    override fun setImage(url: String) {
        dpView.setImageURI(url)
    }

    override fun onImagesLoaded(list: ArrayList<String>) {
        photoRecyler.adapter = ProfilePhotoListAdapter(list)
    }
}
