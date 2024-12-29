import { Dumbbell, Plus } from "lucide-react";
import React from "react";


const NavBar = ({setView ,view}) => {

    return(
        <>
      <div className="bg-white shadow">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="flex h-16 justify-between">
            <div className="flex">
              <div className="flex flex-shrink-0 items-center">
                <Dumbbell className="h-8 w-8 text-blue-600" />
              </div>
              <div className="ml-6 flex space-x-8">
                <button
                  onClick={() => setView('patients')}
                  className={`inline-flex items-center border-b-2 px-1 pt-1 text-sm font-medium ${
                    view === 'patients'
                      ? 'border-blue-500 text-gray-900'
                      : 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700'
                  }`}
                >
                  Patients
                </button>
                <button
                  onClick={() => setView('exercises')}
                  className={`inline-flex items-center border-b-2 px-1 pt-1 text-sm font-medium ${
                    view === 'exercises'
                      ? 'border-blue-500 text-gray-900'
                      : 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700'
                  }`}
                >
                  Exercises
                </button>
              </div>
            </div>
            <div className="flex items-center">
              <button
                onClick={() => setView('newPatient')}
                className="ml-3 inline-flex items-center rounded-md bg-blue-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-blue-500"
              >
                <Plus className="-ml-0.5 mr-1.5 h-5 w-5" />
                New Patient
              </button>
              <button
                onClick={() => setView('newExercise')}
                className="ml-3 inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50"
              >
                <Plus className="-ml-0.5 mr-1.5 h-5 w-5" />
                New Exercise
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
    )
}

export default NavBar;