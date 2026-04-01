

// src/lib/validations/employee.schema.ts
export const createEmployeeSchema = z.object({
    firstName:    z.string().min(2, "Prenom requis (min 2 caracteres)"),
            lastName:     z.string().min(2, "Nom requis (min 2 caracteres)"),
            email:        z.string().email("Adresse email invalide"),
            baseSalary:   z.coerce.number().positive("Salaire doit etre positif"),
            currency:     z.string().length(3, "Code ISO a 3 lettres (ex: XOF, EUR)"),
            contractType: z.enum(["CDI","CDD","FREELANCE","INTERNSHIP","PART_TIME"]),
    contractStartDate: z.string().min(1, "Date de debut requise"),
            contractEndDate:   z.string().optional(),
}).refine(
        // Regle conditionnelle : endDate obligatoire si contrat != CDI
        data => data.contractType === "CDI" || !!data.contractEndDate,
{ message: "Date de fin requise pour CDD/Stage", path: ["contractEndDate"] }
);

// Type TypeScript deduit automatiquement du schema
type CreateEmployeeForm = z.infer<typeof createEmployeeSchema>;