(

ClioLibrary.catalog ({

	~fxReverb = ClioSynthDefFactory(*[
		out:Clio.busses[\master],
		channels:2,

		{ arg name=\fxReverb, kwargs;

			SynthDef(name, { arg room=0.66, mix=0.88, damp=0.2;

				var sig = In.ar(kwargs[\fxBus], kwargs[\channels]);

				sig = FreeVerb2.ar(sig[0], sig[1], room:room, mix:mix, damp:damp);

				Out.ar(kwargs[\out], sig);

			});
		};

	]);

	~fxEcho = ClioSynthDefFactory(*[
		out:Clio.busses[\master],
		channels:2,
		delaytime:0.5,
		decaytime:4,
		mul:0.4,

		{ arg name=\fxEcho, kwargs;

			SynthDef(name, {

				var sig = In.ar(kwargs[\fxBus], kwargs[\channels]);

				sig = CombL.ar(sig,
					maxdelaytime:kwargs[\delaytime],
					delaytime:kwargs[\delaytime],
					decaytime:kwargs[\decaytime],
					mul:kwargs[\mul],
					add:sig);

				Out.ar(kwargs[\out], sig);

			});
		};

	]);


	~fxDualEcho = ClioSynthDefFactory(*[
		out:Clio.busses[\master],
		channels:2,
		delaytime1:0.5,
		delaytime2:1/3,
		decaytime:4,
		mul1:0.4,
		mul2:0.2,

		{ arg name=\fxEcho, kwargs;

			SynthDef(name, {

				var sig = In.ar(kwargs[\fxBus], kwargs[\channels]);

				sig = CombL.ar(sig,
					maxdelaytime:kwargs[\delaytime1],
					delaytime:kwargs[\delaytime1],
					decaytime:kwargs[\decaytime],
					mul:kwargs[\mul1],
					add:sig);
				sig = CombL.ar(sig,
					maxdelaytime:kwargs[\delaytime2],
					delaytime:kwargs[\delaytime2],
					decaytime:kwargs[\decaytime],
					mul:kwargs[\mul2],
					add:sig);

				Out.ar(kwargs[\out], sig);

			});
		};

	]);


}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)




