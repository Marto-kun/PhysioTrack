package main.java.services;

import main.java.modelo.*;

/**
 * Utilidades para preparar objetos antes de persistirlos en MongoDB.
 * Se clonan de forma plana dejando colecciones internas vacías para evitar
 * referencias recursivas y problemas de serialización.
 */
public final class PersistenceSanitizer {

    private PersistenceSanitizer() {}

    public static Fisioterapeuta sanitize(Fisioterapeuta f) {
        if (f == null) return null;
        Fisioterapeuta out = new Fisioterapeuta();
        out.setId(f.getId());
        out.setCedula(f.getCedula());
        out.setNombre(f.getNombre());
        out.setEspecialidad(f.getEspecialidad());
        // pacientesAsignados intentionally left empty to keep document plano
        return out;
    }

    public static Paciente sanitize(Paciente p) {
        if (p == null) return null;
        Paciente out = new Paciente();
        out.setId(p.getId());
        out.setCedula(p.getCedula());
        out.setNombre(p.getNombre());
        out.setEdad(p.getEdad());
        out.setTipoLesion(p.getTipoLesion());
        out.setNivelLesion(p.getNivelLesion());
        out.setFisioterapeutaAsignado(sanitize(p.getFisioterapeutaAsignado()));
        return out;
    }

    public static Insumo sanitize(Insumo i) {
        if (i == null) return null;
        Insumo out = new Insumo(i.getId(), i.getNombre(), i.getFechaElaboracion(), i.getFechaCaducidad(), i.getStock(), i.getStockMinimo());
        return out;
    }

    public static EquipoBiomedico sanitize(EquipoBiomedico e) {
        if (e == null) return null;
        EquipoBiomedico out = new EquipoBiomedico(e.getId(), e.getNombre(), e.getFechaElaboracion(), e.getEstado());
        out.setHorasDeUso(e.getHorasDeUso());
        return out;
    }

    public static PlanRehabilitacion sanitize(PlanRehabilitacion p) {
        if (p == null) return null;

        return new PlanRehabilitacion(
                p.getIdPlan(),
                sanitize(p.getPaciente()),
                p.getEjercicio(),
                p.getSeries(),
                p.getRepeticiones(),
                p.getDiasPorSemana()
        );
    }

    public static SesionTratamiento sanitize(SesionTratamiento s) {
        if (s == null) return null;
        SesionTratamiento out = new SesionTratamiento(
                s.getIdSesion(),
                s.getFecha(),
                sanitize(s.getPaciente()),
                sanitize(s.getFisioterapeutaResponsable()),
                sanitize(s.getEquipo()),
                s.getInsumo(),
                s.getObservaciones(),
                s.getDuracionMinutos());
        return out;
    }
}


