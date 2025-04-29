package org.Traffix.circulation;

import org.Traffix.circulation.GestionnaireAccidents.Accident;

public abstract class ÉcouteurAccidentRetiré {
    public abstract void retiré(Accident a, Route route);
}
