import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SubRuleInListItemService } from '../service/sub-rule-in-list-item.service';

import { SubRuleInListItemComponent } from './sub-rule-in-list-item.component';

describe('Component Tests', () => {
  describe('SubRuleInListItem Management Component', () => {
    let comp: SubRuleInListItemComponent;
    let fixture: ComponentFixture<SubRuleInListItemComponent>;
    let service: SubRuleInListItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SubRuleInListItemComponent],
      })
        .overrideTemplate(SubRuleInListItemComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubRuleInListItemComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SubRuleInListItemService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.subRuleInListItems[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.subRuleInListItems[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalledWith(expect.objectContaining({ sort: ['id,asc'] }));
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['name,asc', 'id'] }));
    });

    it('should re-initialize the page', () => {
      // WHEN
      comp.loadPage(1);
      comp.reset();

      // THEN
      expect(comp.page).toEqual(0);
      expect(service.query).toHaveBeenCalledTimes(2);
      expect(comp.subRuleInListItems[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
