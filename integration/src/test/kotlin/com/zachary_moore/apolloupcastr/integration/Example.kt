package com.zachary_moore.apolloupcastr.integration

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import com.zachary_moore.apolloupcastr.integration.fragment.BasicTweet
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class Example {
    @OptIn(ApolloExperimental::class)
    @Test
    fun query1And2Interop() = runTest {
        val apolloClient = ApolloClient.Builder()
            .networkTransport(QueueTestNetworkTransport())
            .build()
        val dat = GetTweetQuery.Data(
            GetTweetQuery.Tweet(
                "1", "abc", null
            )
        )
        val q1 = GetTweetQuery()
        apolloClient.enqueueTestResponse(q1, dat)
        val r1: GetTweetQuery.Data = apolloClient.query(q1).execute().data!!

        val q2 = GetTweet2Query(id = "1")
        val dat2 = GetTweet2Query.Data(
            GetTweet2Query.Tweet(
                __typename = "nothing",
                basicTweet = BasicTweet("1", "abc", null)
            )
        )

        apolloClient.enqueueTestResponse(q2, dat2)
        val r2 = apolloClient.query(q2).execute().data!!

    }

    private fun utility() {

    }
}