package com.test.aktivocoresdksample_android.ui.social

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.aktivolabs.aktivocore.data.models.feed.Feed
import com.aktivolabs.aktivocore.data.models.feed.enums.FeedStatusEnum
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.*
import com.aktivolabs.aktivocore.data.models.schemas.feed.response.FeedEmbeddedResponse
import com.test.aktivocoresdksample_android.R
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivitySocialBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.*
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class SocialActivity : BaseActivity() {

    companion object {
        val TAG = SocialActivity.javaClass.simpleName
    }

    private lateinit var binding: ActivitySocialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Social"

        binding.actionLayout.actionBtnRV.apply {
            adapter = ActionAdapter(mappingUtils.getSocialActions(), object : ActionListener {
                override fun onActionClick(action: Action) {
                    when (action.actionEnum) {
                        ActionEnum.FEEDS -> {
                            getFeeds()
                        }
                        ActionEnum.COMMENTS_LIKE -> {
                            enterFeedId(FEED_API_GET_COMMENTS)
                        }
                        ActionEnum.POST_COMMENT -> {
                            enterFeedId(FEED_API_POST_COMMENT)
                        }
                        ActionEnum.LIKE_UNLIKE_FEED -> {
                            enterFeedId(FEED_API_LIKE_UNLIKE)
                        }
                        ActionEnum.HIDE_FEED -> {
                            enterFeedId(FEED_API_HIDE)
                        }
                        ActionEnum.UNFOLLOW_USER -> {
                            enterFeedId(FEED_API_UNFOLLOW_USER)
                        }
                        ActionEnum.REPORT_USER -> {
                            enterFeedId(FEED_API_REPORT_USER)
                        }
                        ActionEnum.DELETE_FEED -> {
                            enterFeedId(FEED_API_DELETE)
                        }
                        ActionEnum.POST_IMAGE_FEED -> {
                            enterFileName(UPLOAD_IMAGE_FILE)
                        }
                        ActionEnum.POST_VIDEO_FEED -> {
                            enterFileName(UPLOAD_VIDEO_FILE)
                        }
                        ActionEnum.POST_TEXT_FEED -> {
                            postTextFeed()
                        }
                        ActionEnum.POST_LINK_FEED -> {
                            postLinkFeed()
                        }
                        ActionEnum.UPDATE_IMAGE_FEED -> {
                            enterFileName(UPDATE_FEED_IMAGE_FILE)
                        }
                        ActionEnum.UPDATE_VIDEO_FEED -> {
                            enterFileName(UPDATE_FEED_VIDEO_FILE)
                        }
                        ActionEnum.UPDATE_TEXT_FEED -> {
                            updateTextFeed()
                        }
                        ActionEnum.UPDATE_LINK_FEED -> {
                            updateLinkFeed()
                        }
                    }
                }

            })
        }
    }

    private fun getFeeds() {
        val pageNumber = 1
        val socialFeedQuery = SocialFeedQuery(localRepository.userId, pageNumber)
        aktivoManager.getFeeds(socialFeedQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Feed?>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(feedList: List<Feed?>) {
                    val message = "getFeeds $feedList"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "getFeeds, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun enterFeedId(feedApiCode: Int) {
        val builder = AlertDialog.Builder(this)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialog = builder.create()
        val dialogView: View = inflater.inflate(R.layout.layout_username_token, null)
        val userDetailsTV = dialogView.findViewById<TextView>(R.id.userDetailsTV)
        val submitBtn = dialogView.findViewById<Button>(R.id.submitBtn)
        val userNameET = dialogView.findViewById<EditText>(R.id.userNameET)
        val tokenET = dialogView.findViewById<EditText>(R.id.accessTokenET)
        val selectorCB = dialogView.findViewById<CheckBox>(R.id.selectorCB)
        if (feedApiCode == FEED_API_LIKE_UNLIKE) {
            selectorCB.visibility = View.VISIBLE
        } else {
            selectorCB.visibility = View.GONE
        }
        if (feedApiCode == FEED_API_UNFOLLOW_USER || feedApiCode == FEED_API_REPORT_USER) {
            userNameET.setHint(R.string.feed_author_id)
        } else {
            userNameET.setHint(R.string.feed_id)
        }
        userDetailsTV.setText(R.string.feed_details)
        tokenET.visibility = View.GONE
        submitBtn.setOnClickListener { v: View? ->
            val feedId = userNameET.text.toString().trim { it <= ' ' }
            when (feedApiCode) {
                FEED_API_GET_COMMENTS -> getCommentsLikeList(feedId)
                FEED_API_POST_COMMENT -> postComment(feedId)
                FEED_API_LIKE_UNLIKE -> {
                    var statusEnum = FeedStatusEnum.Like
                    if (!selectorCB.isChecked) statusEnum = FeedStatusEnum.UnLike
                    likeUnlikeFeed(feedId, statusEnum)
                }
                FEED_API_HIDE -> hideFeed(feedId)
                FEED_API_UNFOLLOW_USER -> unFollowUser(feedId)
                FEED_API_REPORT_USER -> reportUser(feedId)
                FEED_API_DELETE -> deleteFeed(feedId)
                else -> Log.w(TAG, "error in feed api code")
            }
            dialog.dismiss()
        }
        dialog.setView(dialogView)
        dialog.show()
    }

    private fun getCommentsLikeList(feedId: String) {
        val feedDetailsQuery = FeedDetailsQuery(feedId)
        aktivoManager.getCommentLikesList(feedDetailsQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<FeedEmbeddedResponse> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(feedembeddedResponse: FeedEmbeddedResponse) {
                    val message = "getCommentsLikeList $feedembeddedResponse"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "getCommentsLikeList, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun postComment(feedId: String) {
        aktivoManager.postCommentToFeed(
            FeedPostCommentQuery(localRepository.userId, feedId, "test 1")
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "comment added successfully"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in posting a comment"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun likeUnlikeFeed(feedId: String, feedStatusEnum: FeedStatusEnum) {
        //String feedId = "603f213a80bd9900142c2ac3";
        aktivoManager.likeUnlikeAFeed(
            FeedLikeDisLikeQuery(localRepository.userId, feedId, feedStatusEnum)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "Like/Unlike added successfully"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in Like/Unlike a feed"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun hideFeed(feedId: String) {
        //String feedId = "6064c6df023dc60012125755";
        aktivoManager.hideFeed(
            HideFeedQuery(localRepository.userId, feedId)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "Hide feed successfully"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in Hide feed"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun unFollowUser(feedAuthorMemberId: String) {
        //String feedAuthorMemberId = "5f9c00f0e278dc001204260f";
        aktivoManager.unFollowUser(
            UnFollowUserQuery(localRepository.userId, feedAuthorMemberId)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "unFollowUser completed successfully"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in unFollowUser"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun reportUser(feedAuthorMemberId: String) {
        //String feedAuthorMemberId = "5f9c00f0e278dc001204260f";
        val reportMessage = "Inappropriate comment"
        aktivoManager.reportUser(
            ReportUserQuery(
                localRepository.userId,
                feedAuthorMemberId,
                ReportUserQuery.ReportCategory.Spam,
                reportMessage
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "reportUser completed successfully"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in reportUser"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun deleteFeed(feedId: String) {
        //String feedId = "606a9a8b023dc6001212639e";
        aktivoManager.deleteFeed(
            DeleteFeedQuery(feedId)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "deleteFeed completed successfully"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in deleteFeed"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun enterFileName(type: Int) {
        val builder = AlertDialog.Builder(this)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialog = builder.create()
        val dialogView = inflater.inflate(R.layout.layout_username_token, null)
        val userDetailsTV = dialogView.findViewById<TextView>(R.id.userDetailsTV)
        val submitBtn = dialogView.findViewById<Button>(R.id.submitBtn)
        val userNameET = dialogView.findViewById<EditText>(R.id.userNameET)
        val tokenET = dialogView.findViewById<EditText>(R.id.accessTokenET)
        val selectorCB = dialogView.findViewById<CheckBox>(R.id.selectorCB)
        selectorCB.visibility = View.GONE
        userNameET.hint = "File name"
        if (type == UPDATE_FEED_IMAGE_FILE || type == UPDATE_FEED_VIDEO_FILE) {
            tokenET.visibility = View.VISIBLE
            tokenET.setHint(R.string.feed_id)
        } else {
            tokenET.visibility = View.GONE
        }
        userDetailsTV.setText(R.string.upload_image_video)
        submitBtn.setOnClickListener { v: View? ->
            val fileName = userNameET.text.toString().trim { it <= ' ' }
            val feedId = tokenET.text.toString().trim { it <= ' ' }
            if (fileName.isEmpty()) return@setOnClickListener
            when (type) {
                UPLOAD_IMAGE_FILE -> postImageFeed(fileName)
                UPLOAD_VIDEO_FILE -> postVideoFeed(fileName)
                UPDATE_FEED_IMAGE_FILE -> {
                    if (feedId.isEmpty()) return@setOnClickListener
                    updateImageFeed(fileName, feedId)
                }
                UPDATE_FEED_VIDEO_FILE -> {
                    if (feedId.isEmpty()) return@setOnClickListener
                    updateVideoFeed(fileName, feedId)
                }
            }
            dialog.dismiss()
        }
        dialog.setView(dialogView)
        dialog.show()
    }

    private fun postImageFeed(fileName: String) {
        //File file = new File(Environment.getExternalStorageDirectory(), "/Aktivo/20210115_144203496.png");
        val file = File(fileName)
        val postImageFeedQuery = PostImageFeedQuery(
            localRepository.userId,
            "sample text",
            file
        )
        aktivoManager.postImageFeed(postImageFeedQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "post image feed done"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in posting image feed"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun postVideoFeed(fileName: String) {
        val file = File(fileName)
        val postVideoFeedQuery = PostVideoFeedQuery(
            localRepository.userId,
            "sample video",
            file
        )
        aktivoManager.postVideoFeed(postVideoFeedQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "posted video feed done"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in posting video feed"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun postTextFeed() {
        val postTextFeedQuery = PostTextFeedQuery(
            localRepository.userId,
            "write something"
        )
        aktivoManager.postTextFeed(postTextFeedQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "post text done"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in post text"
                    Log.e(TAG, message, e)
                    showText(message)

                }
            })
    }

    private fun postLinkFeed() {
        val postLinkFeedQuery = PostLinkFeedQuery(
            localRepository.userId,
            "write something",
            "https://cdn2.iconfinder.com/data/icons/pittogrammi/142/46-512.png",
            "linkTitle",
            "Host for the link",
            "linkDescription",
            "https://www.outlookindia.com/website/story/india-news-covid-19-these-states-require-a-negative-rt-pcr-test-for-entry/379281"
        )
        aktivoManager.postLinkFeed(postLinkFeedQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "posting link done"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in posting link done"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun updateImageFeed(fileName: String, feedId: String) {
        //File file = new File(Environment.getExternalStorageDirectory(), "/Aktivo/e-tax-123.png");
        val file = File(fileName)
        val updateImageFeedQuery = UpdateImageFeedQuery(
            feedId,
            "updated image text",
            file
        )
        aktivoManager.updateImageFeed(updateImageFeedQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "updated image feed done"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in updated image feed"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun updateVideoFeed(fileName: String, feedId: String) {
        //File file = new File(Environment.getExternalStorageDirectory(), "/Aktivo/video.mp4");
        val file = File(fileName)
        val updateVideoFeedQuery = UpdateVideoFeedQuery(
            feedId,
            "updated video text",
            file
        )
        aktivoManager.updateVideoFeed(updateVideoFeedQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "updated video feed done"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in update video feed done"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun updateTextFeed() {
        val updateTextFeedQuery = UpdateTextFeedQuery(
            "socialId",
            "updated text"
        )
        aktivoManager.updateFeedText(updateTextFeedQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "updated text feed done"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in update text feed done"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun updateLinkFeed() {
        val updateFeedLinkQuery = UpdateFeedLinkQuery(
            "socialId",
            "updated text",
            "updateLinkImageUrl",
            "updateLinkTitle",
            "updateLinkHost",
            "updateLinkDescription",
            "updateLink"
        )
        aktivoManager.updateFeedLink(updateFeedLinkQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "updated link feed done"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in update link feed"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }
}