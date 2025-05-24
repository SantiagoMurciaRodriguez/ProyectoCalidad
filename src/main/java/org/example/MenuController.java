package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private static final String IMAGEN_DEFAULT = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS1TfeWRgY2ekpPB2om4uu9ZDjoaAnWnpkC4A&s";
    private static final List<Map<String, Object>> platos = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    static {
        platos.add(new HashMap<>(Map.of(
                "id", idCounter.getAndIncrement(),
                "nombre", "Pasta Alfredo",
                "descripcion", "Deliciosa pasta con salsa cremosa de alfredo, acompañada de pollo y especias.",
                "imagen", IMAGEN_DEFAULT
        )));
        platos.add(new HashMap<>(Map.of(
                "id", idCounter.getAndIncrement(),
                "nombre", "Ensalada César",
                "descripcion", "Fresca mezcla de lechugas, crutones, pollo y aderezo césar.",
                "imagen", IMAGEN_DEFAULT
        )));
        platos.add(new HashMap<>(Map.of(
                "id", idCounter.getAndIncrement(),
                "nombre", "Bife de Chorizo",
                "descripcion", "Jugoso corte de carne asada, acompañado de papas rústicas y vegetales.",
                "imagen", IMAGEN_DEFAULT
        )));
    }

    @GetMapping
    public List<Map<String, Object>> getMenu() {
        return platos;
    }

    @PostMapping
    public Map<String, Object> addPlato(@RequestBody Map<String, String> body) {
        Map<String, Object> nuevoPlato = new HashMap<>();
        nuevoPlato.put("id", idCounter.getAndIncrement());
        nuevoPlato.put("nombre", body.get("nombre"));
        nuevoPlato.put("descripcion", body.get("descripcion"));
        String imagen = body.get("imagen");
        if (imagen == null || imagen.isBlank()) imagen = IMAGEN_DEFAULT;
        nuevoPlato.put("imagen", imagen);
        platos.add(nuevoPlato);
        return nuevoPlato;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePlato(@PathVariable Long id, @RequestBody Map<String, String> body) {
        for (Map<String, Object> plato : platos) {
            if (plato.get("id").equals(id)) {
                plato.put("nombre", body.get("nombre"));
                plato.put("descripcion", body.get("descripcion"));
                String imagen = body.get("imagen");
                if (imagen == null || imagen.isBlank()) imagen = IMAGEN_DEFAULT;
                plato.put("imagen", imagen);
                return ResponseEntity.ok(plato);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlato(@PathVariable Long id) {
        Iterator<Map<String, Object>> iterator = platos.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> plato = iterator.next();
            if (plato.get("id").equals(id)) {
                iterator.remove();
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPlato(@PathVariable Long id) {
        for (Map<String, Object> plato : platos) {
            if (plato.get("id").equals(id)) {
                return ResponseEntity.ok(plato);
            }
        }
        return ResponseEntity.notFound().build();
    }
}