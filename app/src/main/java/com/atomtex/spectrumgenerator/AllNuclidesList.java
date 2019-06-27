package com.atomtex.spectrumgenerator;

import com.atomtex.spectrumgenerator.domain.EnergyLine;
import com.atomtex.spectrumgenerator.domain.Nuclide;

import java.util.ArrayList;
import java.util.List;

import static com.atomtex.spectrumgenerator.domain.Nuclide.Category.I;
import static com.atomtex.spectrumgenerator.domain.Nuclide.Category.M;
import static com.atomtex.spectrumgenerator.domain.Nuclide.Category.N;
import static com.atomtex.spectrumgenerator.domain.Nuclide.State.UNIDENTIFIED;

class AllNuclidesList {

    static List<Nuclide> getAllNuclides() {
        List<Nuclide> nuclides = new ArrayList<>();

        nuclides.add(new Nuclide(
                "241", "Am", I, UNIDENTIFIED, 3, 0, 0,
                new EnergyLine[]{
                        new EnergyLine(120.0f, 2600, 0, 0, 128),
                        new EnergyLine(60.0f, 65534, 65534, 11520, 128),
                        new EnergyLine(26.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "133", "Ba", I, UNIDENTIFIED, 8, 0, 0, new EnergyLine[]{
                new EnergyLine(455.0f, 0, 0, 0, 128),
                new EnergyLine(356.0f, 54319, 65534, 19819, 152),
                new EnergyLine(292.0f, 11681, 0, 0, 128),
                new EnergyLine(157.0f, 0, 0, 0, 128),
                new EnergyLine(118.0f, 5000, 0, 0, 128),
                new EnergyLine(81.0f, 65534, 1, 0, 128),
                new EnergyLine(53.0f, 0, 0, 0, 128),
                new EnergyLine(31.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "109", "Cd", I, UNIDENTIFIED, 2, 0, 0, new EnergyLine[]{
                new EnergyLine(88.0f, 65534, 65534, 0, 128),
                new EnergyLine(22.0f, 42597, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "57", "Co", I, UNIDENTIFIED, 5, 0, 0, new EnergyLine[]{
                new EnergyLine(695.0f, 0, 0, 0, 128),
                new EnergyLine(252.0f, 0, 0, 0, 128),
                new EnergyLine(122.0f, 65534, 65534, 27392, 128),
                new EnergyLine(85.0f, 0, 0, 0, 128),
                new EnergyLine(33.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "60", "Co", I, UNIDENTIFIED, 5, 0, 0, new EnergyLine[]{
                new EnergyLine(1333.0f, 57595, 57595, 31992, 140),
                new EnergyLine(1173.0f, 65534, 65534, 31954, 160),
                new EnergyLine(910.0f, 25000, 0, 0, 128),
                new EnergyLine(224.0f, 0, 0, 0, 128),
                new EnergyLine(78.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "134", "Cs", I, UNIDENTIFIED, 8, 0, 0, new EnergyLine[]{
                new EnergyLine(1367.0f, 0, 0, 0, 128),
                new EnergyLine(1156.0f, 0, 0, 0, 128),
                new EnergyLine(1037.0f, 0, 0, 0, 128),
                new EnergyLine(794.0f, 60492, 65534, 0, 128),
                new EnergyLine(596.0f, 65534, 65534, 0, 128),
                new EnergyLine(475.0f, 0, 0, 0, 128),
                new EnergyLine(385.0f, 0, 0, 0, 128),
                new EnergyLine(199.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "137", "Cs", I, UNIDENTIFIED, 8, 0, 0, new EnergyLine[]{
                new EnergyLine(1320.0f, 150, 0, 0, 128),
                new EnergyLine(662.0f, 65534, 65534, 27232, 128),
                new EnergyLine(442.0f, 0, 0, 0, 128),
                new EnergyLine(320.0f, 1000, 0, 0, 128),
                new EnergyLine(203.0f, 0, 0, 0, 128),
                new EnergyLine(186.0f, 6762, 0, 0, 128),
                new EnergyLine(78.0f, 0, 0, 0, 128),
                new EnergyLine(32.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "152", "Eu", I, UNIDENTIFIED, 13, 0, 0, new EnergyLine[]{
                new EnergyLine(1408.0f, 3801, 0, 6671, 1),
                new EnergyLine(1103.0f, 4900, 0, 0, 128),
                new EnergyLine(964.0f, 0, 0, 0, 128),
                new EnergyLine(778.0f, 4100, 0, 0, 128),
                new EnergyLine(566.0f, 0, 0, 0, 128),
                new EnergyLine(444.0f, 0, 0, 0, 128),
                new EnergyLine(344.0f, 29397, 65534, 8502, 1),
                new EnergyLine(245.0f, 0, 0, 0, 128),
                new EnergyLine(170.0f, 0, 0, 0, 128),
                new EnergyLine(122.0f, 65534, 1, 0, 128),
                new EnergyLine(84.0f, 0, 0, 0, 128),
                new EnergyLine(78.0f, 0, 0, 0, 128),
                new EnergyLine(40.0f, 42869, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "192", "Ir", I, UNIDENTIFIED, 6, 0, 0, new EnergyLine[]{
                new EnergyLine(888.0f, 0, 0, 0, 128),
                new EnergyLine(603.0f, 0, 0, 0, 128),
                new EnergyLine(468.0f, 19000, 65534, 15303, 128),
                new EnergyLine(310.0f, 65534, 54021, 0, 128),
                new EnergyLine(155.0f, 1972, 0, 0, 128),
                new EnergyLine(65.0f, 5605, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "54", "Mn", I, UNIDENTIFIED, 4, 0, 0, new EnergyLine[]{
                new EnergyLine(835.0f, 65534, 65534, 31992, 128),
                new EnergyLine(596.0f, 5133, 0, 0, 128),
                new EnergyLine(202.0f, 0, 0, 0, 128),
                new EnergyLine(78.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "99", "Mo", I, UNIDENTIFIED, 8, 0, 0, new EnergyLine[]{
                new EnergyLine(938.0f, 0, 0, 0, 128),
                new EnergyLine(743.0f, 43384, 65534, 0, 128),
                new EnergyLine(520.0f, 0, 0, 0, 128),
                new EnergyLine(470.0f, 0, 0, 0, 128),
                new EnergyLine(336.0f, 0, 0, 0, 128),
                new EnergyLine(189.0f, 0, 0, 0, 128),
                new EnergyLine(140.0f, 65534, 0, 0, 128),
                new EnergyLine(76.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "22", "Na", I, UNIDENTIFIED, 6, 0, 0, new EnergyLine[]{
                new EnergyLine(1274.0f, 15486, 65534, 31979, 1),
                new EnergyLine(1006.0f, 2500, 0, 0, 128),
                new EnergyLine(511.0f, 65534, 1, 57824, 1),
                new EnergyLine(312.0f, 2169, 0, 0, 128),
                new EnergyLine(177.0f, 0, 0, 0, 128),
                new EnergyLine(78.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "238", "Pu", I, UNIDENTIFIED, 4, 0, 0, new EnergyLine[]{
                new EnergyLine(150.0f, 65534, 65534, 0, 128),
                new EnergyLine(96.0f, 0, 0, 0, 128),
                new EnergyLine(43.0f, 0, 0, 0, 128),
                new EnergyLine(0.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "75", "Se", I, UNIDENTIFIED, 5, 0, 0, new EnergyLine[]{
                new EnergyLine(401.0f, 10000, 56712, 0, 128),
                new EnergyLine(269.0f, 60000, 65534, 0, 128),
                new EnergyLine(199.0f, 0, 0, 0, 128),
                new EnergyLine(133.0f, 65534, 0, 0, 128),
                new EnergyLine(97.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "90", "Sr", I, UNIDENTIFIED, 1, 0, 0, new EnergyLine[]{
                new EnergyLine(0.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "51", "Cr", M, UNIDENTIFIED, 2, 0, 0, new EnergyLine[]{
                new EnergyLine(320.0f, 65534, 65534, 0, 128),
                new EnergyLine(212.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "18", "F", M, UNIDENTIFIED, 4, 0, 0, new EnergyLine[]{
                new EnergyLine(511.0f, 65534, 65534, 0, 128),
                new EnergyLine(312.0f, 2169, 0, 0, 128),
                new EnergyLine(177.0f, 0, 0, 0, 128),
                new EnergyLine(78.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "67", "Ga", M, UNIDENTIFIED, 9, 0, 0, new EnergyLine[]{
                new EnergyLine(888.0f, 0, 0, 0, 128),
                new EnergyLine(794.0f, 0, 0, 0, 128),
                new EnergyLine(495.0f, 0, 0, 0, 128),
                new EnergyLine(393.0f, 1100, 17296, 1496, 128),
                new EnergyLine(300.0f, 200, 65534, 0, 128),
                new EnergyLine(186.0f, 49789, 0, 0, 128),
                new EnergyLine(140.0f, 0, 0, 0, 128),
                new EnergyLine(90.0f, 65534, 0, 0, 128),
                new EnergyLine(75.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "123", "I", M, UNIDENTIFIED, 3, 0, 0, new EnergyLine[]{
                new EnergyLine(159.0f, 65534, 65534, 26656, 128),
                new EnergyLine(78.0f, 0, 0, 0, 128),
                new EnergyLine(27.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "125", "I", M, UNIDENTIFIED, 1, 0, 0, new EnergyLine[]{
                new EnergyLine(27.0f, 65534, 65534, 0, 128)}));
        nuclides.add(new Nuclide(
                "131", "I", M, UNIDENTIFIED, 7, 0, 0, new EnergyLine[]{
                new EnergyLine(732.0f, 0, 0, 0, 128),
                new EnergyLine(636.0f, 1551, 2500, 2278, 128),
                new EnergyLine(364.0f, 65534, 65534, 26111, 128),
                new EnergyLine(284.0f, 7993, 0, 0, 128),
                new EnergyLine(153.0f, 7386, 0, 0, 128),
                new EnergyLine(82.0f, 0, 0, 0, 128),
                new EnergyLine(30.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "111", "In", M, UNIDENTIFIED, 6, 0, 0, new EnergyLine[]{
                new EnergyLine(245.0f, 56388, 65534, 0, 128),
                new EnergyLine(171.0f, 65534, 59630, 29048, 128),
                new EnergyLine(110.0f, 0, 0, 0, 128),
                new EnergyLine(78.0f, 0, 0, 0, 128),
                new EnergyLine(60.0f, 0, 0, 0, 128),
                new EnergyLine(26.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "99m", "Tc", M, UNIDENTIFIED, 3, 0, 0, new EnergyLine[]{
                new EnergyLine(140.0f, 65534, 65534, 28499, 128),
                new EnergyLine(90.0f, 0, 0, 0, 128),
                new EnergyLine(78.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "201", "Tl", M, UNIDENTIFIED, 6, 0, 0, new EnergyLine[]{
                new EnergyLine(438.0f, 0, 0, 0, 128),
                new EnergyLine(363.0f, 0, 0, 0, 128),
                new EnergyLine(167.0f, 33819, 65534, 3200, 128),
                new EnergyLine(128.0f, 0, 0, 0, 128),
                new EnergyLine(77.0f, 65534, 982, 0, 128),
                new EnergyLine(43.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "133", "Xe", M, UNIDENTIFIED, 2, 0, 0, new EnergyLine[]{
                new EnergyLine(82.0f, 65534, 65534, 12159, 128),
                new EnergyLine(31.0f, 52228, 522, 0, 128)}));
        nuclides.add(new Nuclide(
                "40", "K", N, UNIDENTIFIED, 2, 0, 0, new EnergyLine[]{
                new EnergyLine(1461.0f, 65534, 65534, 0, 128),
                new EnergyLine(1144.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "226", "Ra", N, UNIDENTIFIED, 14, 0, 0, new EnergyLine[]{
                new EnergyLine(1758.0f, 0, 0, 0, 128),
                new EnergyLine(1416.0f, 0, 0, 0, 128),
                new EnergyLine(1261.0f, 0, 0, 0, 128),
                new EnergyLine(1120.0f, 0, 0, 0, 128),
                new EnergyLine(764.0f, 0, 0, 0, 128),
                new EnergyLine(609.0f, 53989, 65534, 14272, 128),
                new EnergyLine(476.0f, 0, 0, 0, 128),
                new EnergyLine(352.0f, 65534, 1, 11232, 128),
                new EnergyLine(295.0f, 33016, 0, 0, 128),
                new EnergyLine(242.0f, 15971, 0, 0, 128),
                new EnergyLine(186.0f, 12526, 0, 0, 128),
                new EnergyLine(145.0f, 0, 0, 0, 128),
                new EnergyLine(77.0f, 0, 0, 0, 128),
                new EnergyLine(47.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "228", "Th", N, UNIDENTIFIED, 16, 0, 0, new EnergyLine[]{
                new EnergyLine(1600.0f, 0, 0, 0, 128),
                new EnergyLine(1119.0f, 0, 0, 0, 128),
                new EnergyLine(846.0f, 0, 0, 0, 128),
                new EnergyLine(729.0f, 0, 0, 3782, 128),
                new EnergyLine(583.0f, 13673, 16722, 9855, 128),
                new EnergyLine(510.0f, 0, 0, 0, 128),
                new EnergyLine(403.0f, 0, 0, 0, 128),
                new EnergyLine(384.0f, 0, 0, 0, 128),
                new EnergyLine(345.0f, 10537, 0, 0, 128),
                new EnergyLine(303.0f, 0, 0, 0, 128),
                new EnergyLine(274.0f, 0, 0, 0, 128),
                new EnergyLine(237.0f, 65534, 65534, 15232, 128),
                new EnergyLine(150.0f, 0, 0, 0, 128),
                new EnergyLine(125.0f, 0, 0, 0, 128),
                new EnergyLine(82.0f, 0, 0, 0, 128),
                new EnergyLine(50.0f, 0, 0, 0, 128)}));
        nuclides.add(new Nuclide(
                "232", "Th", N, UNIDENTIFIED, 14, 0, 0, new EnergyLine[]{
                new EnergyLine(2614.0f, 2211, 65534, 0, 128),
                new EnergyLine(1626.0f, 0, 0, 0, 128),
                new EnergyLine(1102.0f, 0, 0, 0, 128),
                new EnergyLine(911.0f, 7945, 1, 0, 128),
                new EnergyLine(738.0f, 0, 0, 0, 128),
                new EnergyLine(503.0f, 0, 0, 0, 128),
                new EnergyLine(403.0f, 0, 0, 0, 128),
                new EnergyLine(384.0f, 0, 0, 0, 128),
                new EnergyLine(338.0f, 5000, 1, 0, 128),
                new EnergyLine(274.0f, 0, 0, 0, 128),
                new EnergyLine(237.0f, 65534, 0, 15232, 128),
                new EnergyLine(125.0f, 0, 0, 0, 128),
                new EnergyLine(77.0f, 0, 0, 0, 128),
                new EnergyLine(61.0f, 0, 0, 0, 128)}));
        return nuclides;



    }

     /* ПАРСЕР В ЛОГ для AScanner

                        *****private List<Nuclide> processIdenResult(NucIdent nuc, Intent intent) {
                        *****List<Nuclide> nuclides = NucIdent.getNuclides();


           for (Nuclide nuclide : nuclides) {
            Log.e(TAG, "nuclides.add(new Nuclide(\n" +
                    "                \""
                    + nuclide.getNumStr() + "\", \""
                    + nuclide.getName() + "\", "
                    + nuclide.getCategory() + ", "
                    + nuclide.getState() + ", "
                    + nuclide.getLinesNum() + ", "
                    + nuclide.getWeight() + ", "
                    + nuclide.getWeightM() + ", new EnergyLine[]{"
            );
            for (int i = 0; i < nuclide.getEnergyLines().length; i++) {
                EnergyLine eLine = nuclide.getEnergyLines()[i];
                Log.e(TAG, "                    new EnergyLine("
                        + eLine.getEnergy() + "f, "
                        + eLine.getFactorsNoShield() + ", "
                        + eLine.getFactorsShield() + ", "
                        + eLine.getQuantumYield() + ", "
                        + eLine.getCorrection() + ")}));"
                );

            }
        }
*/
}
