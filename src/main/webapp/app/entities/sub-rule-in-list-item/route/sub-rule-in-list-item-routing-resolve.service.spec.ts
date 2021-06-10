jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISubRuleInListItem, SubRuleInListItem } from '../sub-rule-in-list-item.model';
import { SubRuleInListItemService } from '../service/sub-rule-in-list-item.service';

import { SubRuleInListItemRoutingResolveService } from './sub-rule-in-list-item-routing-resolve.service';

describe('Service Tests', () => {
  describe('SubRuleInListItem routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SubRuleInListItemRoutingResolveService;
    let service: SubRuleInListItemService;
    let resultSubRuleInListItem: ISubRuleInListItem | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SubRuleInListItemRoutingResolveService);
      service = TestBed.inject(SubRuleInListItemService);
      resultSubRuleInListItem = undefined;
    });

    describe('resolve', () => {
      it('should return ISubRuleInListItem returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubRuleInListItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSubRuleInListItem).toEqual({ id: 123 });
      });

      it('should return new ISubRuleInListItem if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubRuleInListItem = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSubRuleInListItem).toEqual(new SubRuleInListItem());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubRuleInListItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSubRuleInListItem).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
