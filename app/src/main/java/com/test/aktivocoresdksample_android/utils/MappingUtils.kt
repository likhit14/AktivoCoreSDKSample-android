package com.test.aktivocoresdksample_android.utils

import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action


class MappingUtils {

    private fun getAction(actionId: String, actionName: String, actionEnum: ActionEnum): Action {
        return Action(actionId, actionName, actionEnum)
    }

    fun getFeature(): MutableList<Action> {
        val featureActions = mutableListOf<Action>()
        featureActions.add(
            getAction(
                "Authentication",
                "Authentication",
                ActionEnum.FEATURE_AUTHENTICATION
            )
        )
        featureActions.add(getAction("Badges", "Badges", ActionEnum.FEATURE_BADGES))
        featureActions.add(getAction("Challenge", "Challenge", ActionEnum.FEATURE_CHALLENGE))
        featureActions.add(
            getAction(
                "Connected Device",
                "Connected Device",
                ActionEnum.FEATURE_CONNECTED_DEVICE
            )
        )
        featureActions.add(getAction("Data Sync", "Data Sync", ActionEnum.FEATURE_DATA_SYNC))
        featureActions.add(
            getAction(
                "Miscellaneous",
                "Miscellaneous",
                ActionEnum.FEATURE_MISCELLANEOUS
            )
        )
        featureActions.add(
            getAction(
                "Notification",
                "Notification",
                ActionEnum.FEATURE_NOTIFICATION
            )
        )
        featureActions.add(getAction("Personalise", "Personalise", ActionEnum.FEATURE_PERSONALISE))
        featureActions.add(
            getAction(
                "Physical Lifestyle",
                "Physical Lifestyle",
                ActionEnum.FEATURE_PHYSICAL_LIFESTYLE
            )
        )
        featureActions.add(getAction("Profile", "Profile", ActionEnum.FEATURE_PROFILE))
        featureActions.add(getAction("Rise Game", "Rise Game", ActionEnum.FEATURE_RISE_GAME))
        featureActions.add(getAction("Social", "Social", ActionEnum.FEATURE_SOCIAL))
        return featureActions
    }

    fun getAuthenticationActions(): MutableList<Action> {
        val authenticationActions = mutableListOf<Action>()
        authenticationActions.add(
            getAction(
                "Request Permission",
                "Request Permission",
                ActionEnum.REQUEST_PERMISSION
            )
        )
        authenticationActions.add(
            getAction(
                "Authenticate User",
                "Authenticate User",
                ActionEnum.AUTHENTICATE_USER
            )
        )
        authenticationActions.add(
            getAction(
                "Bypass authenticate using token",
                "Bypass authenticate using token",
                ActionEnum.BYPASS_AUTHENTICATE_USER_TOKEN
            )
        )
        return authenticationActions
    }

    fun getBadgesActions(): MutableList<Action> {
        val badgesActions = mutableListOf<Action>()
        badgesActions.add(getAction("Badge By Date", "Badge By Date", ActionEnum.BADGE_BY_DATE))
        badgesActions.add(getAction("Badge Summary", "Badge Summary", ActionEnum.BADGE_SUMMARY))
        badgesActions.add(getAction("Badge History", "Badge History", ActionEnum.BADGE_HISTORY))
        badgesActions.add(getAction("All badges", "All badges", ActionEnum.ALL_BADGES))
        return badgesActions
    }

    fun getChallengesActions(): MutableList<Action> {
        val challengesActions = mutableListOf<Action>()
        challengesActions.add(
            getAction(
                "Get Ongoing Challenges",
                "Get Ongoing Challenges",
                ActionEnum.ONGOING_CHALLENGES
            )
        )
        challengesActions.add(
            getAction(
                "Get Over Challenges",
                "Get Over Challenges",
                ActionEnum.OVER_CHALLENGES
            )
        )
        challengesActions.add(getAction("Get Challenge", "Get Challenge", ActionEnum.CHALLENGE))
        challengesActions.add(
            getAction(
                "Enroll To Challenge",
                "Enroll To Challenge",
                ActionEnum.ENROLL_CHALLENGE
            )
        )
        return challengesActions
    }

    fun getConnectedDevicesActions(): MutableList<Action> {
        val connectedDevicesActions = mutableListOf<Action>()
        connectedDevicesActions.add(
            getAction(
                "Fetch Fitness Trackers",
                "Fetch Fitness Trackers",
                ActionEnum.FITNESS_TRACKER
            )
        )
        connectedDevicesActions.add(
            getAction(
                "Fetch Auth Url for Fitbit",
                "Fetch Auth Url for Fitbit",
                ActionEnum.AUTH_URL_FITBIT
            )
        )
        connectedDevicesActions.add(
            getAction(
                "Fetch Auth Url for Garmin",
                "Fetch Auth Url for Garmin",
                ActionEnum.AUTH_URL_GARMIN
            )
        )
        connectedDevicesActions.add(
            getAction(
                "Disconnect Fitbit",
                "Disconnect Fitbit",
                ActionEnum.DISCONNECT_FITBIT
            )
        )
        connectedDevicesActions.add(
            getAction(
                "Disconnect Garmin",
                "Disconnect Garmin",
                ActionEnum.DISCONNECT_GARMIN
            )
        )
        connectedDevicesActions.add(
            getAction(
                "Invalidate User",
                "Invalidate User",
                ActionEnum.INVALIDATE_USER
            )
        )
        return connectedDevicesActions
    }

    fun getDashboardActions(): MutableList<Action> {
        val dashboardActions = mutableListOf<Action>()
        return dashboardActions
    }

    fun getDataSyncActions(): MutableList<Action> {
        val dataSyncActions = mutableListOf<Action>()
        dataSyncActions.add(
            getAction(
                "Sync Fitness Data",
                "Sync Fitness Data",
                ActionEnum.SYNC_FITNESS_DATA
            )
        )
        dataSyncActions.add(
            getAction(
                "Sync Supplementary Fitness Data",
                "Sync Supplementary Fitness Data",
                ActionEnum.SYNC_SUPPLEMENTARY_FITNESS_DATA
            )
        )
        dataSyncActions.add(
            getAction(
                "Clear database",
                "Clear database",
                ActionEnum.CLEAR_DATABASE
            )
        )
        return dataSyncActions
    }

    fun getMiscellaneousActions(): MutableList<Action> {
        val miscellaneousActions = mutableListOf<Action>()
        return miscellaneousActions
    }

    fun getNotificationActions(): MutableList<Action> {
        val notificationActions = mutableListOf<Action>()
        notificationActions.add(
            getAction(
                "Get Feed Notifications",
                "Get Feed Notifications",
                ActionEnum.FEED_NOTIFICATIONS
            )
        )
        notificationActions.add(
            getAction(
                "Get Unread Notification Count",
                "Get Unread Notification Count",
                ActionEnum.UNREAD_NOTIFICATION_COUNT
            )
        )
        notificationActions.add(
            getAction(
                "Mark Notification As Read",
                "Mark Notification As Read",
                ActionEnum.MARK_NOTIFICATION_READ
            )
        )
        notificationActions.add(
            getAction(
                "Set Sleep Notification Title",
                "Set Sleep Notification Title",
                ActionEnum.SET_SLEEP_NOTIFICATION_TITLE
            )
        )
        notificationActions.add(
            getAction(
                "Set Sleep Notification Description",
                "Set Sleep Notification Description",
                ActionEnum.SET_SLEEP_NOTIFICATION_DESCRIPTION
            )
        )
        notificationActions.add(
            getAction(
                "Set Sleep Notification Icon",
                "Set Sleep Notification Icon",
                ActionEnum.SET_SLEEP_NOTIFICATION_ICON
            )
        )
        notificationActions.add(
            getAction(
                "Set Sleep Notification Channel Name",
                "Set Sleep Notification Channel Name",
                ActionEnum.SET_SLEEP_NOTIFICATION_CHANNEL_NAME
            )
        )
        notificationActions.add(
            getAction(
                "Set Sleep Notification Channel Id",
                "Set Sleep Notification Channel Id",
                ActionEnum.SET_SLEEP_NOTIFICATION_CHANNEL_ID
            )
        )
        return notificationActions
    }

    fun getPersonaliseActions(): MutableList<Action> {
        val personaliseActions = mutableListOf<Action>()
        personaliseActions.add(
            getAction(
                "Call Personalize",
                "Call Personalize",
                ActionEnum.PERSONALIZE
            )
        )
        return personaliseActions
    }

    fun getPhysicalLifeStyleActions(): MutableList<Action> {
        val physicalLifeStyleActions = mutableListOf<Action>()
        physicalLifeStyleActions.add(
            getAction(
                "Get Score Stats",
                "Get Score Stats",
                ActionEnum.SCORE_STATS
            )
        )
        physicalLifeStyleActions.add(
            getAction(
                "Get Steps Stats",
                "Get Steps Stats",
                ActionEnum.STEPS_STATS
            )
        )
        physicalLifeStyleActions.add(
            getAction(
                "Get Sleep Stats",
                "Get Sleep Stats",
                ActionEnum.SLEEP_STATS
            )
        )
        physicalLifeStyleActions.add(
            getAction(
                "Get Heart rate stats",
                "Get Heart rate stats",
                ActionEnum.HEART_RATE_STATS
            )
        )
        physicalLifeStyleActions.add(
            getAction(
                "Get Score Stats Latest",
                "Get Score Stats Latest",
                ActionEnum.LATEST_SCORE_STATS
            )
        )
        physicalLifeStyleActions.add(
            getAction(
                "Batch Query",
                "Batch Query",
                ActionEnum.BATCH_QUERY
            )
        )
        physicalLifeStyleActions.add(
            getAction(
                "Get Score Insight",
                "Get Score Insight",
                ActionEnum.SCORE_INSIGHT
            )
        )
        return physicalLifeStyleActions
    }

    fun getProfileActions(): MutableList<Action> {
        val profileActions = mutableListOf<Action>()
        profileActions.add(
            getAction(
                "Get User Profile",
                "Get User Profile",
                ActionEnum.USER_PROFILE
            )
        )
        profileActions.add(
            getAction(
                "Update User Profile",
                "Update User Profile",
                ActionEnum.UPDATE_USER_PROFILE
            )
        )
        return profileActions
    }

    fun getRiseGameActions(): MutableList<Action> {
        val riseGameActions = mutableListOf<Action>()
        riseGameActions.add(getAction("Get Rise Game", "Get Rise Game", ActionEnum.RISE_GAME))
        riseGameActions.add(
            getAction(
                "Get Rise Game Rewards",
                "Get Rise Game Rewards",
                ActionEnum.RISE_GAME_REWARDS
            )
        )
        riseGameActions.add(
            getAction(
                "Get Daily Rise Game Points",
                "Get Daily Rise Game Points",
                ActionEnum.DAILY_RISE_GAME_POINTS
            )
        )
        riseGameActions.add(
            getAction(
                "Get Weekly Rise Game Points",
                "Get Weekly Rise Game Points",
                ActionEnum.WEEKLY_RISE_GAME_POINTS
            )
        )
        return riseGameActions
    }

    fun getSocialActions(): MutableList<Action> {
        val socialActions = mutableListOf<Action>()
        socialActions.add(getAction("Get Feeds", "Get Feeds", ActionEnum.FEEDS))
        socialActions.add(
            getAction(
                "Get Comments And Like",
                "Get Comments And Like",
                ActionEnum.COMMENTS_LIKE
            )
        )
        socialActions.add(getAction("Post a comment", "Post a comment", ActionEnum.POST_COMMENT))
        socialActions.add(
            getAction(
                "Like or unlike Feed",
                "Like or unlike Feed",
                ActionEnum.LIKE_UNLIKE_FEED
            )
        )
        socialActions.add(getAction("Hide a Feed", "Hide a Feed", ActionEnum.HIDE_FEED))
        socialActions.add(getAction("UnFollow User", "UnFollow User", ActionEnum.UNFOLLOW_USER))
        socialActions.add(getAction("Report User", "Report User", ActionEnum.REPORT_USER))
        socialActions.add(getAction("Delete Feed", "Delete Feed", ActionEnum.DELETE_FEED))
        socialActions.add(
            getAction(
                "Post Image Feed",
                "Post Image Feed",
                ActionEnum.POST_IMAGE_FEED
            )
        )
        socialActions.add(
            getAction(
                "Post Video Feed",
                "Post Video Feed",
                ActionEnum.POST_VIDEO_FEED
            )
        )
        socialActions.add(getAction("Post Text Feed", "Post Text Feed", ActionEnum.POST_TEXT_FEED))
        socialActions.add(getAction("Post Link Feed", "Post Link Feed", ActionEnum.POST_LINK_FEED))
        socialActions.add(
            getAction(
                "Update Image Feed",
                "Update Image Feed",
                ActionEnum.UPDATE_IMAGE_FEED
            )
        )
        socialActions.add(
            getAction(
                "Update Video Feed",
                "Update Video Feed",
                ActionEnum.UPDATE_VIDEO_FEED
            )
        )
        socialActions.add(
            getAction(
                "Update Text Feed",
                "Update Text Feed",
                ActionEnum.UPDATE_TEXT_FEED
            )
        )
        socialActions.add(
            getAction(
                "Update Link Feed",
                "Update Link Feed",
                ActionEnum.UPDATE_LINK_FEED
            )
        )
        return socialActions
    }

}