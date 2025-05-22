package org.muhammedoguz

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.security.MessageDigest

@Serializable
data class User(
    val username: String,
    var password: String,
    val auth: String
)

fun main() {
    val file = File("data/users.json")
    if (!file.exists()) {
        returnFun("❌ File not exists.", "quit")
    } else {
        val content = file.readText()
        val users = Json.decodeFromString<List<User>>(content)
        print("Username:")
        val inputUsername = readln()
        val logUser = users.find { it.username == inputUsername }
        if (logUser == null) {
            returnFun("❌ No such user found.", "quit")
        } else {
            print("Password:")
            val inputPassword = readln().trim()
            val inputPasswordHash = hashPasswd(inputPassword)
            if (logUser.password == inputPasswordHash) {
                println("Welcome to the System.")
                println("Your authentication is ${logUser.auth}")
                if (logUser.auth == "admin") {
                    adminMenu(users, file, logUser)
                    return
                } else {
                    println("Hi ${logUser.username} \uD83D\uDC64")
                    userMenu(logUser, file)

                }
            } else {
                returnFun("❌ Incorrect password.", "quit")
            }
        }
    }
}

fun adminMenu(user: List<User>, file: File, currentUser: User) {
    println("----------------------------")
    println("\uD83D\uDC64 Welcome to your admin panel.")
    while (true) {
        val content = file.readText()
        val user = Json.decodeFromString<List<User>>(content)
        println("----------------------------")
        println("Print Users: 1 \n Add User: 2 \n Remove User: 3 \n Password Change: 4 \n Quit: 0")
        println("----------------------------")
        print("What do you want to do:")
        val adminSel = readln().toIntOrNull()
        when (adminSel) {
            1 -> printUsers(user)
            2 -> addUser(user, file)
            3 -> removeUser(user, file, currentUser)
            4 -> adminPasswdChange(user, file, currentUser)
            0 -> break
            null -> run {
                returnFun("❌ You can't select any option except numbers between 0-4", "return to main menu")
            }
        }
    }
    returnFun("Quitting from the system...", "quit")
}

fun userMenu(currentUser: User, file: File) {
    println("----------------------------")
    println("\uD83D\uDC64 Welcome to your user panel.")
    while (true) {
        val content = file.readText()
        val user = Json.decodeFromString<List<User>>(content)
        println("----------------------------")
        println("Password Change: 1 \n Quit: 0")
        println("----------------------------")
        print("What do you want to do:")
        val adminSel = readln().toIntOrNull()
        when (adminSel) {
            1 -> userPasswdChange(file, currentUser)
            0 -> break
            null -> run {
                returnFun("❌ You can't select any option except numbers between 0-3", "return to menu")
            }
        }
    }
    returnFun("Quitting from the system...", "quit")
}

fun adminPasswdChange(user: List<User>, file: File, currentUser: User) {
    val content = file.readText()
    val user = Json.decodeFromString<List<User>>(content)
    print("Change your password: 1 \n Change password anyone else: 2 \n Quit: 0")
    val selection = readln().toIntOrNull()
    if (selection == 0) return
    if (selection == 1) userPasswdChange(file, currentUser)
    if (selection == 2) {
        println("Users: \n ${printUsers(user)}")
        print("Please enter the index number of user that you want to change password:")
        val choice = readln().toIntOrNull()
        if (choice != null) {
            if (choice in 1..user.size) {
                val mutableList = user.toMutableList()
                val choiceJson = mutableList[choice - 1]
                print("Please enter the new password:")
                val newPassFirst = readLine()
                if (newPassFirst != null) {
                    print("Please enter the new password again:")
                    val newPassSec = readln()
                    if (newPassFirst == newPassSec) {
                        choiceJson.password = newPassSec
                        val updatedList = user.map {
                            if (it.username == choiceJson.username) it.copy(password = hashPasswd(choiceJson.password))
                            else it
                        }
                        val updatedJson = Json.encodeToString(updatedList)
                        file.writeText(updatedJson)
                        println("\uD83D\uDD04 Changes saved successfully.")
                    } else {
                        returnFun("❌ Passwords are not matching. Please try again...", "return to menu")
                    }
                } else {
                    returnFun("❌ New password can't be null. Please try again...", "return to menu")
                }
            } else {
                returnFun(
                    "❌ User index can't be in any index except between ${1..user.size}. Please try again",
                    "return to menu"
                )
            }
        } else {
            returnFun("❌ User index can't be null. Please try again...", "return to menu")
        }
    } else returnFun("❌ You can't select any option except numbers between 0-2", "return to menu")
}

fun printUsers(user: List<User>): Unit {
    user.forEachIndexed { index, user ->
        val baseInfo =
            "${index + 1} - Username: ${user.username}, Password is hashed \uD83D\uDEE1\uFE0F, Auth: ${user.auth}"
        println(baseInfo)
    }
}

fun addUser(user: List<User>, file: File): Unit {
    val mutableList = user.toMutableList()
    print("Enter username of the user that you want to add:")
    val newUsername = readLine()
    if (user.any { it.username == newUsername }) {
        returnFun("❌ A user with that username already exists.", "return to menu")
    } else {
        if (!newUsername.isNullOrBlank()) {
            print("Enter a password of user that you want to add[min. 6 digits]:")
            val newPassword = readLine()
            if (newPassword != null) {
                val newPasswordHash = hashPasswd(newPassword)
                if (newPassword.length >= 6) {
                    print("Enter auth of the user that you want to add[admin/user]:")
                    val newAuth = readln().lowercase().toString()
                    if (newAuth == "admin" || newAuth == "user") {
                        val newUser = User(newUsername, newPasswordHash, newAuth)
                        mutableList.add(newUser)
                        val updatedJson = Json.encodeToString(mutableList)
                        file.writeText(updatedJson)

                        println("\uD83D\uDD04 Changes saved successfully.")
                    } else {
                        returnFun("❌ Auth can't be null or anything except admin and user.", "return to menu")
                    }
                } else {
                    returnFun("❌ Password can't be null or shorter than 6 digits.", "return to menu")
                }
            } else {
                returnFun("❌ Auth can't be null", "return to menu")
            }
        } else {
            returnFun("❌ Username can't be null or blank.", "return to menu")
        }
    }

}

fun removeUser(user: List<User>, file: File, currentUser: User): Unit {
    val mutableList = user.toMutableList()
    println("Users:")
    printUsers(user)
    println("----------------------------------------------")
    print("Enter the index of user that you want to remove:")
    val selection = readln().toIntOrNull()
    if (selection != null) {
        if (selection in 1..user.size) {
            val userToRemove = user[selection - 1]
            if (userToRemove.username == currentUser.username) {
                returnFun("❌ You cannot remove the account you're currently logged in with.", "return to menu")
            }
            val removedUser = mutableList.removeAt(selection - 1)
            println("${removedUser.username} is removed from list.")
            val updatedJson = Json.encodeToString(mutableList)
            file.writeText(updatedJson)
            println("\uD83D\uDD04 Changes saved successfully.")
        } else {
            returnFun("❌ User index can't be in any index except between ${1..user.size}", "return to menu")
        }
    } else {
        returnFun("❌ Invalid Selection", "return to menu")
    }
}

fun userPasswdChange(file: File, logUser: User): Unit {
    val content = file.readText()
    val user = Json.decodeFromString<List<User>>(content)
    print("Please enter your current password:")
    val currentPass = readLine()
    if (currentPass != null) {
        val currentPassHash = hashPasswd(currentPass)
        if (currentPassHash == logUser.password) {
            print("Please enter your new password:")
            val newPassFirst = readLine()
            if (newPassFirst != null) {
                print("Please enter your new password again:")
                val newPassSec = readLine()
                if (newPassFirst == newPassSec) {
                    logUser.password = newPassSec
                    val updatedList = user.map {
                        if (it.username == logUser.username) it.copy(password = hashPasswd(logUser.password))
                        else it
                    }
                    val updatedJson = Json.encodeToString(updatedList)
                    file.writeText(updatedJson)
                    println("\uD83D\uDD04 Changes saved successfully.")
                } else {
                    returnFun("❌ Passwords are not matching. Please try again...", "return to menu")
                }
            } else {
                returnFun("❌ Your new password can't be null. Please try again...", "return to menu")
            }
        } else {
            returnFun("❌ Your current password is not ${currentPass}. Please try again...", "return to menu")
        }
    } else {
        returnFun("❌ Your current password can't be null. Please try again...", "return to menu")
    }
}

fun hashPasswd(password: String): String {
    val bytes = password.toByteArray()
    val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}

fun returnFun(message: String, location: String = "continue"): Unit {
    println(message)
    println("[Press Enter to ${location}...]")
    readln()
    return
}