package org.muhammedoguz

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

@Serializable
data class User(
    val username: String,
    val password: String,
    val auth: String
)

fun main() {
    val file = File("data/users.json")
    if (!file.exists()) {
        println("File not exists.")
        println("[Press enter to quit...]")
        readln()
        return
    } else {
        val content = file.readText()
        val users = Json.decodeFromString<List<User>>(content)
        print("Username:")
        val inputUsername = readln()
        val logUser = users.find { it.username == inputUsername }
        if (logUser == null) {
            println("No such user found.")
            println("[Press Enter to quit...]")
            readln()
            return
        } else {
            print("Password:")
            val inputPassword = readln()
            if (logUser.password == inputPassword) {
                println("Welcome to the System.")
                println("Your authentication is ${logUser.auth}")
                if (logUser.auth == "admin") {
                    adminMenu(users, file, logUser)
                    return
                } else {
                    println("Hi ${logUser.username}.")
                    println("[Press Enter to quit...]")
                    readln()
                    return
                }
            }
        }
    }
}

fun adminMenu(user: List<User>, file: File, currentUser: User) {
    println("----------------------------")
    println("Welcome to your admin panel.")
    while (true) {
        println("----------------------------")
        println("Print Users: 1 \n Add User: 2 \n Remove User: 3 \n Quit: 0")
        println("----------------------------")
        print("What do you want to do:")
        val adminSel = readln().toIntOrNull()
        when (adminSel) {
            1 -> printUsers(user)
            2 -> addUser(user, file)
            3 -> removeUser(user, file, currentUser)
            0 -> break
        }
    }
    println("Quitting from the system...")
    println("[Press Enter to quit..].")
    readln()
    return
}

fun printUsers(user: List<User>): Unit {
    println("Do you want to print user with password[Y/n]")
    val choice = readln().lowercase()

    user.forEachIndexed { index, user ->
        val baseInfo = "${index + 1} - Username: ${user.username}, Auth: ${user.auth}"
        val withPass = if (choice == "y") ", Password: ${user.password}" else ""
        println(baseInfo + withPass)
    }



}

fun addUser(user: List<User>, file: File): Unit {
    val mutableList = user.toMutableList()
    print("Enter username of the user that you want to add:")
    val newUsername = readLine()
    if (user.any { it.username == newUsername }) {
        println("A user with that username already exists.")
        println("[Press Enter to return to menu...]")
        readln()
        return
    }
    if (newUsername != null) {
        print("Enter a password of user that you want to add[min. 6 digits]:")
        val newPassword = readLine()
        if (newPassword != null && newPassword.length >= 6) {
            print("Enter auth of the user that you want to add[admin/user]:")
            val newAuth = readLine()
            if (newAuth != null && (newAuth == "admin" || newAuth == "user")) {
                val newUser = User(newUsername, newPassword, newAuth)
                mutableList.add(newUser)
                val updatedJson = Json.encodeToString(mutableList)
                file.writeText(updatedJson)

                println("Changes saved successfully.")
            } else {
                println("Auth can't be null and anything except admin and user.")
                println("[Press Enter to return to menu...]")
                readln()
            }
        } else {
            println("Password can't be null or shorter than 6 digits.")
            println("[Press Enter to return to menu...]")
            readln()
        }
    } else {
        println("Username can't be null.")
        println("[Press Enter to return to menu...]")
        readln()
    }
}

fun removeUser(user: List<User>, file: File, currentUser: User): Unit {
    val mutableList = user.toMutableList()

    print("Enter the index of user that you want to remove:")
    val selection = readln().toIntOrNull()
    if (selection != null && selection in 1..user.size) {
        val userToRemove = user[selection - 1]
        if (userToRemove.username == currentUser.username) {
            println("You cannot remove the account you're currently logged in with.")
            println("[Press Enter to return to menu...]")
            readln()
            return
        }
        val removedUser = mutableList.removeAt(selection - 1)
        println("${removedUser.username} is removed from list.")
        val updatedJson = Json.encodeToString(mutableList)
        file.writeText(updatedJson)
        println("Changes saved successfully.")
    } else {
        println("Invalid Selection")
        println("[Press Enter to return to menu...]")
        readln()
    }
}