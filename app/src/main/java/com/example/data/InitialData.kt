package com.example.data

object InitialData {
    val physicsChapters = listOf(
        "Units, Dimensions & Measurements",
        "Mathematical Tools & Vectors",
        "Motion in a Straight Line",
        "Motion in a Plane",
        "Laws of Motion",
        "Work, Energy & Power",
        "Centre of Mass & System of Particles",
        "Rotational Motion",
        "Rotational Motion & Rotational Dynamics",
        "Mechanical Properties of Solids",
        "Thermal Properties of Matter",
        "Kinetic Theory of Thermodynamics",
        "Mechanical Properties of Fluids",
        "Oscillations",
        "Waves",
        "Electric Charges & Fields",
        "Electrostatic Potential & Capacitance",
        "Current Electricity",
        "Moving Charges & Magnetism",
        "Magnetism & Matter",
        "Electromagnetic Induction",
        "Alternating Current",
        "Electromagnetic Waves",
        "Ray Optics & Optical Instruments",
        "Wave Optics",
        "Dual Nature of Radiation & Matter",
        "Atoms",
        "Nuclei",
        "Semiconductor Electronics: Materials, Devices & Simple Circuits"
    ).mapIndexed { index, name ->
        ChapterEntity(
            id = index + 1,
            subject = SubjectType.PHYSICS,
            sNo = index + 1,
            name = name
        )
    }

    val chemistryChapters = listOf(
        "Some Basic Concepts of Chemistry",
        "Structure of Atom",
        "Thermodynamics & Thermochemistry",
        "Equilibrium",
        "Electrochemistry",
        "Chemical Kinetics",
        "Surface Chemistry",
        "Redox Reaction",
        "Solutions",
        "Coordination Compounds",
        "Principles of Qualitative Analysis",
        "p-Block Elements",
        "The d- and f-Block Elements",
        "Some Basic Principles & Techniques (IUPAC Naming)",
        "Isomerism",
        "Hydrocarbons",
        "Haloalkanes & Haloarenes",
        "Alcohols, Phenols & Ethers",
        "Aldehydes, Ketones & Carboxylic Acids",
        "Amines",
        "Biomolecules",
        "Purification Methods"
    ).mapIndexed { index, name ->
        ChapterEntity(
            id = 29 + index + 1,
            subject = SubjectType.CHEMISTRY,
            sNo = index + 1,
            name = name
        )
    }

    val mathematicsChapters = listOf(
        "Sequence & Series",
        "Quadratic Equations",
        "Complex Numbers",
        "Binomial Theorem",
        "Straight Lines",
        "Circles",
        "Conic Sections: Parabola",
        "Conic Sections: Hyperbola",
        "Conic Sections: Ellipse",
        "Complex Number",
        "Statistics",
        "Trigonometric Functions",
        "Trigonometric Equation",
        "Solutions of Triangle",
        "Determinants",
        "Matrices",
        "Vector Algebra",
        "Three Dimensional Geometry",
        "Sets & Relations",
        "Functions",
        "Inverse Trigonometric Functions",
        "Limits, Continuity & Differentiability",
        "Method of Differentiation",
        "Application of Derivatives",
        "Indefinite Integration",
        "Application of Integrals",
        "Differential Equation",
        "Probability"
    ).mapIndexed { index, name ->
        ChapterEntity(
            id = 51 + index + 1,
            subject = SubjectType.MATHEMATICS,
            sNo = index + 1,
            name = name
        )
    }

    val tests = listOf(
        TestEntity(id = 1, sNo = 1, name = "JEE Mains-1", type = "Part Test", pattern = "JEE Mains", date = "11 Oct 2026"),
        TestEntity(id = 2, sNo = 2, name = "JEE Mains-2", type = "Part Test", pattern = "JEE Mains", date = "18 Oct 2026"),
        TestEntity(id = 3, sNo = 3, name = "JEE Mains-3", type = "Part Test", pattern = "JEE Mains", date = "25 Oct 2026"),
        TestEntity(id = 4, sNo = 4, name = "JEE Mains-4", type = "Part Test", pattern = "JEE Mains", date = "1 Nov 2026"),
        TestEntity(id = 5, sNo = 5, name = "JEE Mains-5", type = "Part Test", pattern = "JEE Mains", date = "22 Nov 2026"),
        TestEntity(id = 6, sNo = 6, name = "JEE Mains-6", type = "Part Test", pattern = "JEE Mains", date = "29 Nov 2026"),
        TestEntity(id = 7, sNo = 7, name = "JEE Mains-7", type = "Part Test", pattern = "JEE Mains", date = "6 Dec 2026"),
        TestEntity(id = 8, sNo = 8, name = "JEE Mains-8", type = "Part Test", pattern = "JEE Mains", date = "13 Dec 2026"),
        TestEntity(id = 9, sNo = 9, name = "JEE Mains-9", type = "Part Test", pattern = "JEE Mains", date = "20 Dec 2026"),
        TestEntity(id = 10, sNo = 10, name = "JEE Mains-10", type = "Part Test", pattern = "JEE Mains", date = "27 Dec 2026"),
        TestEntity(id = 11, sNo = 11, name = "JEE Mains-11", type = "Part Test", pattern = "JEE Mains", date = "3 Jan 2027"),
        TestEntity(id = 12, sNo = 12, name = "JEE Mains-12", type = "Part Test", pattern = "JEE Mains", date = "10 Jan 2027"),
        TestEntity(id = 13, sNo = 13, name = "AITS-1", type = "Full Test", pattern = "Main", date = "13 Jan 2027"),
        TestEntity(id = 14, sNo = 14, name = "AITS-2", type = "Full Test", pattern = "Main", date = "15 Jan 2027"),
        TestEntity(id = 15, sNo = 15, name = "AITS-3", type = "Full Test", pattern = "Main", date = "20 Jan 2027"),
        TestEntity(id = 16, sNo = 16, name = "AITS-4", type = "Full Test", pattern = "Main", date = "24 Jan 2027"),
        TestEntity(id = 17, sNo = 17, name = "AITS-5", type = "Full Test", pattern = "Main", date = "31 Jan 2027"),
        TestEntity(id = 18, sNo = 18, name = "AITS-6", type = "Full Test", pattern = "Main", date = "7 Feb 2027"),
        TestEntity(id = 19, sNo = 19, name = "AITS-7", type = "Full Test", pattern = "Main", date = "14 Feb 2027"),
        TestEntity(id = 20, sNo = 20, name = "AITS-8", type = "Full Test", pattern = "Main", date = "21 Feb 2027"),
        TestEntity(id = 21, sNo = 21, name = "AITS-9", type = "Full Test", pattern = "Main", date = "28 Feb 2027"),
        TestEntity(id = 22, sNo = 22, name = "AITS-10", type = "Full Test", pattern = "Main", date = "7 Mar 2027"),
        TestEntity(id = 23, sNo = 23, name = "AITS-11", type = "Full Test", pattern = "Main", date = "14 Mar 2027"),
        TestEntity(id = 24, sNo = 24, name = "AITS-12", type = "Full Test", pattern = "Main", date = "21 Mar 2027"),
        TestEntity(id = 25, sNo = 25, name = "AITS-13", type = "Full Test", pattern = "Main", date = "28 Mar 2027"),
        TestEntity(id = 26, sNo = 26, name = "AITS-14", type = "Full Test", pattern = "Main", date = "4 Apr 2027"),
        TestEntity(id = 27, sNo = 27, name = "AITS-15", type = "Full Test", pattern = "JEE Main", date = "11 Apr 2027"),
        TestEntity(id = 28, sNo = 28, name = "AITS-16", type = "Full Test", pattern = "JEE Main", date = "18 Apr 2027"),
        TestEntity(id = 29, sNo = 29, name = "AITS-17", type = "Full Test", pattern = "JEE Main", date = "25 Apr 2027"),
        TestEntity(id = 30, sNo = 30, name = "AITS-18", type = "Full Test", pattern = "JEE Advanced", date = "2 May 2027"),
        TestEntity(id = 31, sNo = 31, name = "AITS-19", type = "Full Test", pattern = "JEE Advanced", date = "9 May 2027"),
        TestEntity(id = 32, sNo = 32, name = "AITS-20", type = "Full Test", pattern = "JEE Advanced", date = "12 May 2027")
    )

    val weeklyReviews = (1..8).map { week ->
        WeeklyReviewEntity(
            weekNumber = week,
            studyHours = if (week == 1) 48.5f else 0f,
            lecturesDone = if (week == 1) 12 else 0,
            dppDone = if (week == 1) 10 else 0,
            pyqDone = if (week == 1) 8 else 0,
            testsDone = if (week == 1) 1 else 0,
            avgScore = if (week == 1) 195f else 0f,
            consistency = if (week == 1) 9 else 8,
            notes = if (week == 1) "Solid kickoff week! Need more problem solving speed." else ""
        )
    }
}
