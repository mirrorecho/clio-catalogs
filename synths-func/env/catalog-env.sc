(

ClioLibrary.catalog ([\func, \env], {

	~perc = ClioSynthFunc({ arg kwargs;

		var env, level = kwargs[\synth][\amp] ? 0.9;

		env = EnvGen.kr(Env.perc(
			attackTime: kwargs[\attackTime],
			releaseTime: kwargs[\releaseTime],
			level:level,
			curve: kwargs[\curve],
		), doneAction:kwargs[\doneAction]);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] * env;

	}, *[ // sets default kwargs:
		doneAction:2,
		attackTime:0.01,
		releaseTime:2,
		curve:-4,
	]);

	// =====================================================================================

	~asr = ClioSynthFunc({ arg kwargs;

		var sustainLevel = kwargs[\synth][\amp] ? 0.9;

		var env = EnvGen.kr(Env.asr(
			attackTime: kwargs[\attackTime],
			sustainLevel: sustainLevel,
			releaseTime: kwargs[\releaseTime],
			curve: kwargs[\curve],
		), gate:kwargs[\synth][\gate], doneAction:kwargs[\doneAction]);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] * env;

	}, *[ // sets default kwargs:
		doneAction:2,
		attackTime:0.01,
		releaseTime:2,
		curve:-4,
	]);

	// =====================================================================================

	~adsr = ClioSynthFunc({ arg kwargs;

		var peakLevel = kwargs[\synth][\amp] ? 0.9;

		var env = EnvGen.kr(Env.adsr(
			attackTime: kwargs[\attackTime],
			decayTime: kwargs[\decayTime],
			sustainLevel: kwargs[\sustainLevel],
			releaseTime: kwargs[\releaseTime],
			peakLevel: peakLevel,
			curve: kwargs[\curve],
		), gate:kwargs[\synth][\gate], doneAction:kwargs[\doneAction]);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] * env;

	}, *[ // sets default kwargs:
		doneAction:2,
		attackTime:0.01,
		decayTime:0.3,
		sustainLevel:0.6,
		releaseTime:2,
		curve:-4,
	]);

	// =====================================================================================

	// same a ~perc with different defaults, but worth defining
	// separately since it's very useful
	~swell = ~perc.mimic(*[attackTime:2, releaseTime:0.01, curve:2]);

	// =====================================================================================

}, { arg key, env;
	// f = env.asDict[\perc];
	// env[\perc].();
});


)

