package com.example.frc_elo_app

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

var redAlliance = Array(3){emptyTeam}
var blueAlliance = Array(3){emptyTeam}

class runMatch1 : AppCompatActivity() {

    var counter = 0
    var displayString = ""
    var inputName = ""
    var inputNumber = 0
    var redAlliancesDisplay = ""
    var blueAlliancesDisplay = ""

    var driverStationInt = 1
    var allianceInt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_match1)
        counter = 0
        displayString = "Enter the team number in RED alliance station 1: "
        findViewById<TextView>(R.id.tv_prompt).text = displayString
        findViewById<TextView>(R.id.tv_prompt).setTextColor(Color.parseColor("#FF4043"))

        findViewById<Button>(R.id.but_enter).visibility = View.VISIBLE
        findViewById<EditText>(R.id.et_team_num).visibility = View.VISIBLE
        findViewById<Button>(R.id.but_enter_num).visibility = View.INVISIBLE
        findViewById<EditText>(R.id.et_team_name).visibility = View.INVISIBLE

        redAlliance = Array(3){emptyTeam}
        blueAlliance = Array(3){emptyTeam}
    }

    fun teamEntered(view: View) {
        findViewById<Button>(R.id.but_enter).visibility = View.VISIBLE
        findViewById<EditText>(R.id.et_team_num).visibility = View.VISIBLE
        findViewById<Button>(R.id.but_enter_num).visibility = View.INVISIBLE
        findViewById<EditText>(R.id.et_team_name).visibility = View.INVISIBLE

        if (counter > 4) {
            counter = 0
            runningPtTwo()
        }

        var userInput = findViewById<EditText>(R.id.et_team_num).text.toString()
        inputNumber = userInput.toInt()

        if (redAlliance.find{it.number == inputNumber} != null || blueAlliance.find{it.number == inputNumber} != null) {
            Toast.makeText(this, "Can't have the same team twice", Toast.LENGTH_LONG).show()

            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        if (teamsByRank.find {it.number == inputNumber} != null) {
            val foundName = teamsByRank.find {it.number == inputNumber}!!.name
            val foundRating = teamsByRank.find {it.number == inputNumber}!!.rating
            if (allianceInt == 0) {
                redAlliancesDisplay = redAlliancesDisplay + "Team $inputNumber, team $foundName " +
                        "with an elo rating of ${foundRating.roundToInt()}\n"
            } else {
                blueAlliancesDisplay = blueAlliancesDisplay + "Team $inputNumber, team $foundName " +
                        "with an elo rating of ${foundRating.roundToInt()}\n"
            }
            findViewById<TextView>(R.id.tv_red_teams).text = redAlliancesDisplay
            findViewById<TextView>(R.id.tv_blue_teams).text = blueAlliancesDisplay

            //Keep track of who is on which alliance
            if (allianceInt == 0) {
                redAlliance[driverStationInt - 1] = teamsByRank.find {it.number == inputNumber}!!
            } else {
                blueAlliance[driverStationInt - 1] = teamsByRank.find {it.number == inputNumber}!!
            }

            Toast.makeText(this, "FOUND", Toast.LENGTH_SHORT).show()

            finishIt()
        } else {
            Toast.makeText(this, "Please enter the name of team $inputNumber", Toast.LENGTH_LONG).show()

            findViewById<Button>(R.id.but_enter).visibility = View.INVISIBLE
            findViewById<EditText>(R.id.et_team_num).visibility = View.INVISIBLE
            findViewById<Button>(R.id.but_enter_num).visibility = View.VISIBLE
            findViewById<EditText>(R.id.et_team_name).visibility = View.VISIBLE
        }
    }

    fun finishIt() {
        counter++

        driverStationInt = counter % 3 + 1
        allianceInt = counter / 3

        if (allianceInt == 0) {
            findViewById<TextView>(R.id.tv_prompt).setTextColor(Color.parseColor("#FF4043"))
        } else {
            findViewById<TextView>(R.id.tv_prompt).setTextColor(Color.parseColor("#2575FF"))
        }

        displayString = "Enter the team number in ${if(allianceInt == 0) "RED" else "BLUE"} " +
                "alliance station $driverStationInt: "
        findViewById<TextView>(R.id.tv_prompt).text = displayString

        findViewById<EditText>(R.id.et_team_num).text.clear()
        findViewById<EditText>(R.id.et_team_name).text.clear()
    }

    fun newTeam(view: View) {
        inputName = findViewById<EditText>(R.id.et_team_name).text.toString()

        val newTeam = Team(inputNumber, inputName)

        teamsByRank.add(0, newTeam)

        if (allianceInt == 0) {
            redAlliancesDisplay = redAlliancesDisplay + "Team $inputNumber, team ${newTeam.name} " +
                    "with an elo rating of ${newTeam.rating.roundToInt()}\n"
        } else {
            blueAlliancesDisplay = blueAlliancesDisplay + "Team $inputNumber, team ${newTeam.name} " +
                    "with an elo rating of ${newTeam.rating.roundToInt()}\n"
        }

        findViewById<TextView>(R.id.tv_red_teams).text = redAlliancesDisplay
        findViewById<TextView>(R.id.tv_blue_teams).text = blueAlliancesDisplay

        //Keep track of who is on which alliance
        if (allianceInt == 0) {
            redAlliance[driverStationInt - 1] = teamsByRank.find {it.number == inputNumber}!!
        } else {
            blueAlliance[driverStationInt - 1] = teamsByRank.find {it.number == inputNumber}!!
        }

        Toast.makeText(this, "TEAM ADDED", Toast.LENGTH_LONG).show()

        finishIt()

        findViewById<Button>(R.id.but_enter).visibility = View.VISIBLE
        findViewById<EditText>(R.id.et_team_num).visibility = View.VISIBLE
        findViewById<Button>(R.id.but_enter_num).visibility = View.INVISIBLE
        findViewById<EditText>(R.id.et_team_name).visibility = View.INVISIBLE
    }

    fun runningPtTwo() {
        saveAll()
        val partTwoIntent = Intent(this, run_match_two::class.java)
        startActivity(partTwoIntent)
    }
}
