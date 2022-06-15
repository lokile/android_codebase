package com.lokile.applibraries.utils.media

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import pl.droidsonroids.gif.GifDrawable

fun String.isWebP() = endsWith(".webp", true)

fun String.isGif() = endsWith(".gif", true)

fun String.isPng() = endsWith(".png", true)

fun String.isApng() = endsWith(".apng", true)

fun String.isJpg() = endsWith(".jpg", true) or endsWith(".jpeg", true)

fun String.isSvg() = endsWith(".svg", true)


fun Context.loadMediaItem(
    path: String,
    target: ImageView,
    signature: String,
    cropThumbnails: Boolean,
    roundCorners: Boolean,
    cornerSize: Int
) {
    if (path.isGif()) {
        try {
            val gifDrawable = GifDrawable(path)
            target.setImageDrawable(gifDrawable)
            gifDrawable.start()

            target.scaleType = if (cropThumbnails) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
            return
        } catch (e: Exception) {

        }
    }
    if (path.isSvg()) {
        target.scaleType = if (cropThumbnails) {
            ImageView.ScaleType.CENTER_CROP
        } else {
            ImageView.ScaleType.FIT_CENTER
        }
    }

    val options =
        RequestOptions()
            .signature(ObjectKey(signature))
            .let {
                if (path.isSvg()) {
                    it
                } else {
                    it.skipMemoryCache(false)
                        .priority(Priority.LOW)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .let {
                            if (path.isPng()) {
                                it.format(DecodeFormat.PREFER_ARGB_8888)
                            } else {
                                it
                            }
                        }
                        .let {
                            if (cropThumbnails) {
                                it.centerCrop()
                            } else {
                                it.fitCenter()
                            }
                        }
                }
            }

    Glide.with(applicationContext)
        .let {
            if (path.isPng() || path.isGif()) {
                it.asBitmap().load(path)
            } else if (path.isSvg()) {
                it.`as`(PictureDrawable::class.java)
                    .load(path)
                    .transition(DrawableTransitionOptions.withCrossFade())
            } else {
                it.load(path)
                    .transition(DrawableTransitionOptions.withCrossFade())
            }
        }
        .apply(options)
        .let {
            if (roundCorners && cornerSize > 0) {
                it.transform(CenterCrop(), RoundedCorners(cornerSize))
            } else {
                it
            }
        }
        .into(target)
}

