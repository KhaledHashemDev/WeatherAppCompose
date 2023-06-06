package com.khaled.weather

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class CoroutineDispatcherRule : TestRule {

    private val testCoroutineDispatcher = StandardTestDispatcher()


    override fun apply(base: Statement, description: Description): Statement = object: Statement() {
        override fun evaluate() {
            Dispatchers.setMain(testCoroutineDispatcher)
            base.evaluate()
            Dispatchers.resetMain()
        }
    }


}