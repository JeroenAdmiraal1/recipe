package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.NotesCommand;
import guru.springframework.recipe.domain.Notes;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesToNodesCommand implements Converter<Notes, NotesCommand> {

	@Nullable
	@Synchronized
	@Override
	public NotesCommand convert(Notes notes) {
		if (notes == null){
			return null;
		}

		final NotesCommand notesCommand = new NotesCommand();
		notesCommand.setId(notes.getId());
		notesCommand.setRecipeNotes(notes.getRecipeNotes());
		return notesCommand;
	}
}
