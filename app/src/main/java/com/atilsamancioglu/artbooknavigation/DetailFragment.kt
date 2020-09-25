package com.atilsamancioglu.artbooknavigation


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_detail.*
import java.io.ByteArrayOutputStream


class DetailFragment : Fragment() {

    var selectedPicture : Uri? = null
    var selectedBitmap : Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener { save(view) }
        imageView.setOnClickListener { selectImage(view) }

        arguments?.let {
            val info = DetailFragmentArgs.fromBundle(it).info
            if (info.equals("new")) {
                //NEW
                artText.setText("")
                artistText.setText("")
                yearText.setText("")
                button.visibility = View.VISIBLE

                val selectedImageBackground = BitmapFactory.decodeResource(context?.resources,R.drawable.selectimage)
                imageView.setImageBitmap(selectedImageBackground)


            } else {
                //OLD

                button.visibility = View.INVISIBLE

                val selectedId = DetailFragmentArgs.fromBundle(it).id
                val database = context?.openOrCreateDatabase("Arts", Context.MODE_PRIVATE,null)

                val cursor = database!!.rawQuery("SELECT * FROM arts WHERE id = ?", arrayOf(selectedId.toString()))

                val artNameIx = cursor.getColumnIndex("artname")
                val artistNameIx = cursor.getColumnIndex("artistname")
                val yearIx = cursor.getColumnIndex("year")
                val imageIx = cursor.getColumnIndex("image")

                while (cursor.moveToNext()) {
                    artText.setText(cursor.getString(artNameIx))
                    artistText.setText(cursor.getString(artistNameIx))
                    yearText.setText(cursor.getString(yearIx))

                    val byteArray = cursor.getBlob(imageIx)
                    val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                    imageView.setImageBitmap(bitmap)

                }

                cursor.close()


            }
        }

    }

    fun save(view: View) {

        val artName = artText.text.toString()
        val artistName = artistText.text.toString()
        val year = yearText.text.toString()

        if (selectedBitmap != null) {
            val smallBitmap = makeSmallerBitmap(selectedBitmap!!,300)

            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            try {

                val database = context?.openOrCreateDatabase("Arts", Context.MODE_PRIVATE, null)
                database?.execSQL("CREATE TABLE IF NOT EXISTS arts (id INTEGER PRIMARY KEY, artname VARCHAR, artistname VARCHAR, year VARCHAR, image BLOB)")

                val sqlString =
                    "INSERT INTO arts (artname, artistname, year, image) VALUES (?, ?, ?, ?)"
                val statement = database!!.compileStatement(sqlString)
                statement.bindString(1, artName)
                statement.bindString(2, artistName)
                statement.bindString(3, year)
                statement.bindBlob(4, byteArray)

                statement.execute()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            val action = DetailFragmentDirections.actionDetailFragmentToArtList()
            Navigation.findNavController(view).navigate(action)


        }
    }

    fun makeSmallerBitmap(image: Bitmap, maximumSize : Int) : Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1) {
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image,width,height,true)

    }


    fun selectImage(view: View) {

        activity?.let {
            if(ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions( arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentToGallery,2)
            }

        }



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {

            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentToGallery,2)
            }

        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if ( requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPicture = data.data

            try {

                if (selectedPicture != null) {

                    if (Build.VERSION.SDK_INT >= 28) {
                        val source =
                            ImageDecoder.createSource(context!!.contentResolver, selectedPicture!!)
                        selectedBitmap = ImageDecoder.decodeBitmap(source)
                        imageView.setImageBitmap(selectedBitmap)
                    } else {
                        selectedBitmap =
                            MediaStore.Images.Media.getBitmap(context!!.contentResolver, selectedPicture)
                        imageView.setImageBitmap(selectedBitmap)
                    }
                }
            } catch (e: Exception) {

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }
}
