package com.example.myapplication.modeles.objects

import com.example.myapplication.modeles.enums.Type
import com.example.myapplication.modeles.enums.Type.*

object CalculEfficacite {

    fun getMultiplicateur(attaque: Type, defense: Type): Double {
        return when (attaque) {
            NORMAL -> when (defense) {
                ROCHE, ACIER -> 0.5
                SPECTRE -> 0.0
                else -> 1.0
            }
            FEU -> when (defense) {
                PLANTE, GLACE, INSECTE, ACIER -> 2.0
                FEU, EAU, ROCHE, DRAGON -> 0.5
                else -> 1.0
            }
            EAU -> when (defense) {
                FEU, SOL, ROCHE -> 2.0
                EAU, PLANTE, DRAGON -> 0.5
                else -> 1.0
            }
            PLANTE -> when (defense) {
                EAU, SOL, ROCHE -> 2.0
                FEU, PLANTE, POISON, VOL, INSECTE, DRAGON, ACIER -> 0.5
                else -> 1.0
            }
            ELECTRIQUE -> when (defense) {
                EAU, VOL -> 2.0
                PLANTE, ELECTRIQUE, DRAGON -> 0.5
                SOL -> 0.0
                else -> 1.0
            }
            GLACE -> when (defense) {
                PLANTE, SOL, VOL, DRAGON -> 2.0
                FEU, EAU, GLACE, ACIER -> 0.5
                else -> 1.0
            }
            COMBAT -> when (defense) {
                NORMAL, GLACE, ROCHE, TENEBRE, ACIER -> 2.0
                POISON, VOL, PSY, INSECTE, FEE -> 0.5
                SPECTRE -> 0.0
                else -> 1.0
            }
            POISON -> when (defense) {
                PLANTE, FEE -> 2.0
                POISON, SOL, ROCHE, SPECTRE -> 0.5
                ACIER -> 0.0
                else -> 1.0
            }
            SOL -> when (defense) {
                FEU, ELECTRIQUE, POISON, ROCHE, ACIER -> 2.0
                PLANTE, INSECTE -> 0.5
                VOL -> 0.0
                else -> 1.0
            }
            VOL -> when (defense) {
                PLANTE, COMBAT, INSECTE -> 2.0
                ELECTRIQUE, ROCHE, ACIER -> 0.5
                else -> 1.0
            }
            PSY -> when (defense) {
                COMBAT, POISON -> 2.0
                PSY, ACIER -> 0.5
                TENEBRE -> 0.0
                else -> 1.0
            }
            INSECTE -> when (defense) {
                PLANTE, PSY, TENEBRE -> 2.0
                FEU, COMBAT, POISON, VOL, SPECTRE, ACIER, FEE -> 0.5
                else -> 1.0
            }
            ROCHE -> when (defense) {
                FEU, GLACE, VOL, INSECTE -> 2.0
                COMBAT, SOL, ACIER -> 0.5
                else -> 1.0
            }
            SPECTRE -> when (defense) {
                PSY, SPECTRE -> 2.0
                TENEBRE -> 0.5
                NORMAL -> 0.0
                else -> 1.0
            }
            DRAGON -> when (defense) {
                DRAGON -> 2.0
                ACIER -> 0.5
                FEE -> 0.0
                else -> 1.0
            }
            TENEBRE -> when (defense) {
                PSY, SPECTRE -> 2.0
                COMBAT, TENEBRE, FEE -> 0.5
                else -> 1.0
            }
            ACIER -> when (defense) {
                GLACE, ROCHE, FEE -> 2.0
                FEU, EAU, ELECTRIQUE, ACIER -> 0.5
                else -> 1.0
            }
            FEE -> when (defense) {
                COMBAT, DRAGON, TENEBRE -> 2.0
                FEU, POISON, ACIER -> 0.5
                else -> 1.0
            }
        }
    }
}