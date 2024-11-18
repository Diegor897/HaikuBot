package haikubot;

import java.util.ArrayList;
import java.util.List;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.tweet.TweetParameters;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.dto.tweet.TweetParameters.Reply;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import haikubot.extra.Pair;

public final class TweetGateway {
	
	public static final String _botHandle = "@HaikuBotUCM";
	
	private static final double _startTimeMinutes = 5.5;
	
	private static TweetGateway _instance;
	
	private static TwitterClient _twitterClient;
	
	private static String _user;
	
	private TweetGateway() {
		_user = "1489919234759139328";

		_twitterClient = new TwitterClient(TwitterCredentials.builder()
				.accessToken("1489919234759139328-ZYIK0kCfS8rOeCOTW5f7uLHr2gQV1a")
                .accessTokenSecret("rW25QfTd99CVpMlT8tJenJVcOJ1X5EsTXmBdCdniT5Z7H")
                .apiKey("fhzBY1fbgyDOfOLwY9GE6Cjjp")
                .apiSecretKey("fn0te8eVsqaqd6uJe6DkTh3lFCabGAFLWULqk5CfZRmObluwTr")
                .bearerToken("AAAAAAAAAAAAAAAAAAAAAMNfZQEAAAAAh0zjrCfXOXv516PhaDR5cNXn4rM%3D7TJbR0jACunF4sakBdhFlS75Mnbi4hEdd0uAAxXyHIjwDuMKry")
                .build());
	}
	
	public static TweetGateway get() {
		if (_instance == null) {
			_instance = new TweetGateway();
		}
		
		return _instance;
	}
	
	public List<TweetV2.TweetData> getRecentTweets(String query, boolean recursive, int results) throws Exception {
		if (results < 10 || results > 100) {
			throw new Exception("Invalid argument in recentTweets(), results: 10 <= results <= 100");
		} else {
			return _twitterClient.searchTweets(query, 
					AdditionalParameters.builder()
					.recursiveCall(recursive)
					.maxResults(results)
					.startTime(TimerService.getTime(_startTimeMinutes))
					.endTime(TimerService.getTime(0))
					.build()).getData();
		}
	}
	
	public List<Pair<TweetV2.TweetData, TweetV2.TweetData>> getMentionedTweets(boolean recursive, int results) {
		List<Pair<TweetV2.TweetData, TweetV2.TweetData>> result = new ArrayList<>();
		List<TweetV2.TweetData> tweets = _twitterClient.getUserMentions(_user, 
																		AdditionalParameters.builder()
																			.recursiveCall(recursive)
																			.maxResults(results)
																			.startTime(TimerService.getTime(_startTimeMinutes))
																			.endTime(TimerService.getTime(0))
																			.build()).getData();
		
		if (tweets != null) {
			for (TweetV2.TweetData tweet : tweets) {
				
				String id = tweet.getInReplyToStatusId();
				
				if (id == null) {
					result.add(new Pair<>(tweet, tweet));
				} else {
					result.add(new Pair<>((TweetV2.TweetData)_twitterClient.getTweet(id), tweet));
				}
			}
		}
		
		// First element is the tweet to analyze, if the mentioned tweet is as reply,
		// first element will be the original tweet in thread, if not it will be the
		// mentioning tweet.
		return result;
	}
	
	public void answer(TweetV2.TweetData tweet, String text, boolean answer) {
		if (answer) {
			_twitterClient.postTweet(TweetParameters.builder()
					.text(text)
					.reply(Reply.builder()
							.inReplyToTweetId(tweet.getId())
							.build())
					.build());
		}
	}
}