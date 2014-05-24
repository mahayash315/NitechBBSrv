package models.service.BBItemAppendix;

import models.entity.BBItemAppendix;
import models.entity.BBItemHead;
import models.service.Model.ModelService;
import play.Logger;

public class BBItemAppendixModelService implements ModelService<Long, BBItemAppendix> {

	public static BBItemAppendixModelService use() {
		return new BBItemAppendixModelService();
	}
	
	@Override
	public BBItemAppendix findById(Long id) {
		if (id != null) {
			return BBItemAppendix.find.byId(id);
		}
		return null;
	}

	@Override
	public BBItemAppendix save(BBItemAppendix entry) {
		if (entry != null) {
			Logger.info("BBItemAppendixModelService#save(entry): saving");
			entry.save();
			Logger.info("BBItemAppendixModelService#save(entry): done");
			if (entry.getId() != null) {
				Logger.info("BBItemAppendixModelService#save(entry): ok, id="+entry.getId());
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBItemAppendix update(BBItemAppendix entry) {
		if (entry != null) {
			entry.update();
			return entry;
		}
		return null;
	}

	@Override
	public BBItemAppendix update(BBItemAppendix entry, Long id) {
		if (entry != null && id != null) {
			Logger.info("BBItemAppendixModelService#update(entry,id): updating id="+id);
			entry.update(id);
			Logger.info("BBItemAppendixModelService#update(entry,id): done");
			if (entry.getId().equals(id)) {
				Logger.info("BBItemAppendixModelService#update(entry,id): ok");
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(BBItemAppendix entry) {
		if (entry != null) {
			entry.delete();
		}
	}

	
	public BBItemAppendix findByHead(BBItemHead head) {
		if (head != null) {
			return BBItemAppendix.find
						.where()
							.eq("head", head)
						.findUnique();
		}
		return null;
	}
}
