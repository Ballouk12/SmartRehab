import React, { useEffect, useState } from "react";

const ExerciesProgramList = ({ programId }) => {
  const [exercises, setExercises] = useState([]);
  useEffect(() => {
    const fetchExercises = async () => {
      try {
        const response = await fetch(
          `http://localhost:8083/api/exercices/program/${programId}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
            credentials: "include", // Inclure les informations d'identification si nécessaire
          }
        );

        if (!response.ok) {
          throw new Error(`Erreur: ${response.status}`);
        }

        const data = await response.json();
        setExercises(data); // Retourne les exercices récupérés
      } catch (error) {
        console.error("Erreur lors de la récupération des exercices :", error);
        throw error; // Relance l'erreur pour la gestion en amont
      }
    };
    fetchExercises();
  }, []);

    const handleDelete = async (id) => {
      try {
        const response = await fetch(`http://localhost:8083/api/exercices/${id}`, {
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
        });
  
        if (!response.ok) throw new Error(`Erreur lors de la suppression : ${response.status}`);
  
        setExercises((prev) => prev.filter((exercise) => exercise.exercice.id !== id));

      } catch (error) {
        console.error("Erreur lors de la suppression :", error);
      }
    };
  

  return (
    <>
       <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        {exercises.map((exercise) => (
          <div key={exercise.exercice.id} className="bg-white shadow rounded-lg overflow-hidden">
            <div className="h-48 overflow-hidden">
              <img
                className="w-full h-full object-cover"
                src={`data:image/png;Base64,${exercise.image}`}
                alt="image"
              />
            </div>
            <div className="p-4">
              <h3 className="text-lg font-medium text-gray-900">
                {exercise.exercice.nom}
              </h3>
              <p className="mt-2 text-sm text-gray-500">
                {exercise.exercice.description}
              </p>
              {exercise.vedio && (
                <a href="#" className="mt-4 inline-block text-blue-600 hover:text-blue-500 font-medium transition-colors">
                  Watch Video
                </a>
              )}
            </div>
          </div>
        ))}
      </div>
    
    </>
  );
};

export default ExerciesProgramList;
