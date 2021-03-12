package com.rain.baselib.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import java.io.Serializable

/**
 *  Create by rain
 *  Date: 2020/7/3
 *  intent启动扩展类
 */
inline fun <reified T : Activity> Fragment.startAc(vararg params: Pair<String, Any?>) {
	AnkoInternals.internalStartActivity(requireContext(), T::class.java, params)
}

inline fun <reified T : Activity> Context.startAc(vararg params: Pair<String, Any?>) =
		AnkoInternals.internalStartActivity(this, T::class.java, params)


inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
		AnkoInternals.createIntent(this, T::class.java, params)

fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

fun Intent.clearStartTask(): Intent = apply {
	addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
	addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

object AnkoInternals {
	@JvmStatic
	fun <T> createIntent(ctx: Context, clazz: Class<out T>, params: Array<out Pair<String, Any?>>): Intent {
		val intent = Intent(ctx, clazz)
		if (params.isNotEmpty()) fillIntentArguments(intent, params)
		return intent
	}
	
	@JvmStatic
	fun internalStartActivity(ctx: Context, activity: Class<out Activity>, params: Array<out Pair<String, Any?>>) {
		ctx.startActivity(createIntent(ctx, activity, params))
	}
//
//	@JvmStatic
//	fun internalStartActivityForResult(act: Activity, activity: Class<out Activity>, requestCode: Int, params: Array<out Pair<String, Any?>>) {
//		act.startActivityForResult(createIntent(act, activity, params), requestCode)
//	}
	
	@JvmStatic
	private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
		params.forEach {
			when (val value = it.second) {
				null -> intent.putExtra(it.first, null as Serializable?)
				is Int -> intent.putExtra(it.first, value)
				is Long -> intent.putExtra(it.first, value)
				is CharSequence -> intent.putExtra(it.first, value)
				is String -> intent.putExtra(it.first, value)
				is Float -> intent.putExtra(it.first, value)
				is Double -> intent.putExtra(it.first, value)
				is Char -> intent.putExtra(it.first, value)
				is Short -> intent.putExtra(it.first, value)
				is Boolean -> intent.putExtra(it.first, value)
				is Serializable -> intent.putExtra(it.first, value)
				is Bundle -> intent.putExtra(it.first, value)
				is Parcelable -> intent.putExtra(it.first, value)
				is Array<*> -> when {
					value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
					value.isArrayOf<String>() -> intent.putExtra(it.first, value)
					value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
					else -> throw Throwable("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
				}
				is IntArray -> intent.putExtra(it.first, value)
				is LongArray -> intent.putExtra(it.first, value)
				is FloatArray -> intent.putExtra(it.first, value)
				is DoubleArray -> intent.putExtra(it.first, value)
				is CharArray -> intent.putExtra(it.first, value)
				is ShortArray -> intent.putExtra(it.first, value)
				is BooleanArray -> intent.putExtra(it.first, value)
				else -> throw Throwable("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
			}
			return@forEach
		}
	}
}

class InternalStartContract(private val activity: Class<out Activity>) : ActivityResultContract<Array<out Pair<String, Any?>>, Intent>() {
	override fun createIntent(context: Context, input: Array<out Pair<String, Any?>>) = AnkoInternals.createIntent(context, activity, input)
	override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
		return if (resultCode == Activity.RESULT_OK) intent else null
	}
}

class InternalStartNoIntentContract(private val activity: Class<out Activity>) : ActivityResultContract<Array<out Pair<String, Any?>>, Boolean>() {
	override fun createIntent(context: Context, input: Array<out Pair<String, Any?>>) = AnkoInternals.createIntent(context, activity, input)
	override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
		return resultCode == Activity.RESULT_OK
	}
}


inline fun <reified T : Activity> Fragment.createRegisterForActivity(crossinline block: (Intent?) -> Unit) = registerForActivityResult(InternalStartContract(T::class.java)) {
	block(it)
}

inline fun <reified T : Activity> AppCompatActivity.createRegisterForActivity(crossinline block: (Intent?) -> Unit) = registerForActivityResult(InternalStartContract(T::class.java)) {
	block(it)
}

inline fun <reified T : Activity> Fragment.createRegisterNoIntentActivity(crossinline block: (Boolean) -> Unit) = registerForActivityResult(InternalStartNoIntentContract(T::class.java)) {
	block(it)
}

inline fun <reified T : Activity> AppCompatActivity.createRegisterNoIntentActivity(crossinline block: (Boolean) -> Unit) = registerForActivityResult(InternalStartNoIntentContract(T::class.java)) {
	block(it)
}

fun ActivityResultLauncher<Array<out Pair<String, Any?>>>.startAcResult(vararg params: Pair<String, Any?>) {
	launch(params)
}

fun Activity.navigationPopUpTo(home_id: Int, navigateId: Int) {
	val findNavController = findNavController(home_id)
	findNavController.navigate(navigateId, null, NavOptions.Builder().setPopUpTo(findNavController.graph.id, true).build())
}

fun Fragment.navigationPopUpTo(navigateId: Int) {
	val findNavController = findNavController()
	findNavController.navigate(navigateId, null, NavOptions.Builder().setPopUpTo(findNavController.graph.id, true).build())
}
